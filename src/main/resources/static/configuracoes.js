document.addEventListener('DOMContentLoaded', () => {
    
    console.log("Script de configurações carregado.");

    // Segurança: Só Master (4)
    if (localStorage.getItem('tipoProfi') !== '4') {
        alert("Acesso Negado.");
        window.location.href = '/pages/tela-principal.html';
        return;
    }

    // Identifica o sistema
    const sistema = localStorage.getItem('sistemaSelecionado') || localStorage.getItem('userSystem') || "BIOMEDICINA";
    document.getElementById('nome-sistema').textContent = sistema;

    // --- TEMA PADRÃO ODONTOLOGIA (Extraído do arquivo do seu amigo) ---
    const ODONTO_THEME = {
        colorPrimary: '#5fb3d3',         // --primary / --header-bg
        colorSecondary: '#4a90a4',       // --primary-dark / --nav-button-hover
        colorText: '#1f2937',            // --text
        fontFamily: "'Inter', sans-serif",
        fontSize: '16px'
    };

    // --- EXIBIR BOTÃO APENAS PARA ODONTOLOGIA ---
    const btnOdonto = document.getElementById('btn-odonto');
    
    if (sistema === "ODONTOLOGIA" && btnOdonto) {
        btnOdonto.style.display = "block"; 
        
        btnOdonto.addEventListener('click', () => {
            if(confirm("Aplicar o tema visual da Odontologia?")) {
                // Preenche os campos com os valores do padrão Odonto
                document.getElementById('inputCor').value = ODONTO_THEME.colorPrimary;
                document.getElementById('inputCorSec').value = ODONTO_THEME.colorSecondary;
                document.getElementById('inputCorTexto').value = ODONTO_THEME.colorText;
                document.getElementById('inputFonte').value = ODONTO_THEME.fontFamily;
                document.getElementById('inputFontSize').value = ODONTO_THEME.fontSize;

                // Atualiza o preview visual na hora
                document.getElementById('header-card').style.backgroundColor = ODONTO_THEME.colorPrimary;
                document.getElementById('preview-box-container').style.backgroundColor = ODONTO_THEME.colorPrimary;
                
                alert("Cores aplicadas! Clique em SALVAR ALTERAÇÕES para confirmar.");
            }
        });
    }

    // 1. CARREGAR DADOS DO SERVIDOR
    fetch(`http://localhost:8080/api/config/${sistema}`)
        .then(res => res.ok && res.status !== 204 ? res.json() : null)
        .then(config => {
            if (config) {
                if (config.colorPrimary) {
                    document.getElementById('inputCor').value = config.colorPrimary;
                    document.getElementById('header-card').style.backgroundColor = config.colorPrimary;
                    document.getElementById('preview-box-container').style.backgroundColor = config.colorPrimary;
                }
                if (config.colorSecondary) document.getElementById('inputCorSec').value = config.colorSecondary;
                if (config.colorText) document.getElementById('inputCorTexto').value = config.colorText;
                if (config.fontFamily) document.getElementById('inputFonte').value = config.fontFamily;
                if (config.fontSize) document.getElementById('inputFontSize').value = config.fontSize;
                if (config.language) document.getElementById('inputLanguage').value = config.language;
                if (config.logoBase64) document.getElementById('previewLogo').src = config.logoBase64;
            }
        });

    // Preview da Imagem
    document.getElementById('inputLogo').addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = e => document.getElementById('previewLogo').src = e.target.result;
            reader.readAsDataURL(file);
        }
    });

    // 2. SALVAR
    document.getElementById('config-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        let logoBase64 = document.getElementById('previewLogo').src;
        // Evita salvar URL vazia ou a própria página como imagem
        if (logoBase64.includes(window.location.host) && logoBase64.length < 500) logoBase64 = null;

        const configData = {
            sistema: sistema,
            colorPrimary: document.getElementById('inputCor').value,
            colorSecondary: document.getElementById('inputCorSec').value,
            colorText: document.getElementById('inputCorTexto').value,
            fontFamily: document.getElementById('inputFonte').value,
            fontSize: document.getElementById('inputFontSize').value,
            language: document.getElementById('inputLanguage').value,
            logoBase64: logoBase64
        };

        console.log("Salvando configuração:", configData);

        const res = await fetch('http://localhost:8080/api/config', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(configData)
        });

        if (res.ok) {
            alert("Configurações salvas com sucesso!");
            window.location.href = '/pages/tela-principal.html';
        } else {
            alert("Erro ao salvar.");
        }
    });

    // 3. RESETAR (Padrão Geral)
    document.getElementById('btn-reset').addEventListener('click', async () => {
        if(confirm("Voltar ao padrão original do sistema (Preto/Verde)?")) {
            await fetch(`http://localhost:8080/api/config/${sistema}`, { method: 'DELETE' });
            alert("Restaurado!");
            window.location.href = '/pages/tela-principal.html';
        }
    });
});