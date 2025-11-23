document.addEventListener('DOMContentLoaded', () => {
    
    // --- DEBUG: VERIFICA SE O ARQUIVO NOVO CARREGOU ---
    console.log("!!! CARREGADO SCRIPT NOVO (INGLÊS) !!!");

    if (localStorage.getItem('tipoProfi') !== '4') {
        alert("Acesso Negado.");
        window.location.href = '/pages/tela-principal.html';
        return;
    }

    const sistema = localStorage.getItem('sistemaSelecionado') || localStorage.getItem('userSystem') || "BIOMEDICINA";
    document.getElementById('nome-sistema').textContent = sistema;

    // 1. CARREGAR (Lê do Java em Inglês)
    fetch(`http://localhost:8080/api/config/${sistema}`)
        .then(res => res.ok && res.status !== 204 ? res.json() : null)
        .then(config => {
            if (config) {
                // Atenção: Java manda 'colorPrimary', 'colorSecondary'
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

    document.getElementById('inputLogo').addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = e => document.getElementById('previewLogo').src = e.target.result;
            reader.readAsDataURL(file);
        }
    });

    // 2. SALVAR (Envia para o Java em INGLÊS)
    document.getElementById('config-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        let logoBase64 = document.getElementById('previewLogo').src;
        if (logoBase64.includes(window.location.host) && logoBase64.length < 500) logoBase64 = null;

        // AQUI ESTÁ A CORREÇÃO: AS CHAVES DEVEM SER EM INGLÊS
        const configData = {
            sistema: sistema,
            colorPrimary: document.getElementById('inputCor').value,      // <-- INGLÊS
            colorSecondary: document.getElementById('inputCorSec').value, // <-- INGLÊS
            colorText: document.getElementById('inputCorTexto').value,    // <-- INGLÊS
            fontFamily: document.getElementById('inputFonte').value,
            fontSize: document.getElementById('inputFontSize') ? document.getElementById('inputFontSize').value : '16px',
            language: document.getElementById('inputLanguage').value,
            logoBase64: logoBase64
        };

        console.log("Enviando dados CORRETOS:", configData); // Verifique no console

        const res = await fetch('http://localhost:8080/api/config', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(configData)
        });

        if (res.ok) {
            alert("Salvo com sucesso!");
            window.location.href = '/pages/tela-principal.html';
        } else {
            alert("Erro ao salvar.");
        }
    });

    document.getElementById('btn-reset').addEventListener('click', async () => {
        if(confirm("Voltar ao padrão?")) {
            await fetch(`http://localhost:8080/api/config/${sistema}`, { method: 'DELETE' });
            alert("Restaurado!");
            window.location.href = '/pages/tela-principal.html';
        }
    });
});