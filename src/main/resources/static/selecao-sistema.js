document.addEventListener('DOMContentLoaded', () => {

    const tipoProfi = localStorage.getItem('tipoProfi');
    const isSystemAdmin = localStorage.getItem('isSystemAdmin') === 'true';
    const userEspecialidades = JSON.parse(localStorage.getItem('userEspecialidades') || '[]');

    // --- 1. GUARDA DE SEGURANÇA ATUALIZADA ---
    // Permite entrar se for Admin Geral (isSystemAdmin) OU Master (4)
    if (!isSystemAdmin && tipoProfi !== '4') {
        alert("Acesso negado. Apenas Administradores ou Masters podem acessar esta página.");
        window.location.href = '/index.html';
        return;
    }

    // --- 2. LÓGICA DOS BOTÕES DE SELEÇÃO ---
    const buttonContainer = document.querySelector('.d-grid.gap-3');
    const allButtons = document.querySelectorAll('.system-select-btn');

    // Se for MASTER (Tipo 4), precisamos filtrar os botões
    if (tipoProfi === '4' && !isSystemAdmin) {
        
        // Esconde todos os botões primeiro
        allButtons.forEach(btn => btn.style.display = 'none');

        // Mostra apenas os botões das especialidades do Master
        userEspecialidades.forEach(espec => {
            const systemName = espec.nome.toUpperCase(); // Ex: "BIOMEDICINA"
            
            // Tenta encontrar um botão existente para esse sistema
            const targetBtn = document.querySelector(`.system-select-btn[data-system="${systemName}"]`);
            
            if (targetBtn) {
                targetBtn.style.display = 'block'; // Mostra o botão
            } else {
                // Opcional: Se não existir botão (ex: um curso novo), cria um aviso ou botão genérico
                console.warn(`Nenhum botão encontrado para o sistema: ${systemName}`);
            }
        });
    } 
    // Se for Admin Geral, ele já vê todos os botões (padrão do HTML)


    // --- 3. AÇÃO DO CLIQUE NOS BOTÕES ---
    allButtons.forEach(button => {
        button.addEventListener('click', () => {
            const selectedSystem = button.getAttribute('data-system');
            
            console.log(`Usuário selecionou o sistema: ${selectedSystem}`);

            // Salva a escolha
            localStorage.setItem('sistemaSelecionado', selectedSystem);

            // Redireciona para a tela principal
            window.location.href = '/pages/tela-principal.html';
        });
    });

    // --- 4. LÓGICA DO LOGOUT ---
    const logoutButton = document.getElementById('admin-logout');
    if (logoutButton) {
        logoutButton.addEventListener('click', (event) => {
            event.preventDefault();
            localStorage.clear();
            window.location.href = '/index.html';
        });
    }
});