document.addEventListener('DOMContentLoaded', () => {

    // --- 1. GUARDA DE SEGURANÇA ---
    // Verifica se o usuário é realmente um admin.
    // Se não for, chuta ele de volta para o login.
    if (localStorage.getItem('isSystemAdmin') !== 'true') {
        alert("Acesso negado.");
        window.location.href = '/index.html';
        return;
    }

    // --- 2. LÓGICA DOS BOTÕES DE SELEÇÃO ---
    // Pega todos os botões que têm a classe 'system-select-btn'
    const systemButtons = document.querySelectorAll('.system-select-btn');

    // Adiciona um "escutador" de clique em cada botão
    systemButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Pega o nome do sistema do atributo 'data-system' do botão
            const selectedSystem = button.getAttribute('data-system');
            
            console.log(`Admin selecionou o sistema: ${selectedSystem}`);

            // Salva a escolha do admin no localStorage
            localStorage.setItem('sistemaSelecionado', selectedSystem);

            // Redireciona para a tela principal
            window.location.href = '/pages/tela-principal.html';
        });
    });

    // --- 3. LÓGICA DO LOGOUT ---
    const logoutButton = document.getElementById('admin-logout');
    logoutButton.addEventListener('click', (event) => {
        event.preventDefault();
        
        // Limpa tudo e volta para o login
        localStorage.clear();
        window.location.href = '/index.html';
    });

});