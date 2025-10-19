document.addEventListener('DOMContentLoaded', () => {
    
    // --- 1. PROTEÇÃO DA PÁGINA (código antigo) ---
    if (localStorage.getItem('userIsLoggedIn') !== 'true') {
        alert("Acesso negado. Por favor, faça o login.");
        window.location.href = '/index.html'; 
        return; 
    }

    // --- 2. PREENCHER DADOS DO USUÁRIO (Novo) ---
    const userName = localStorage.getItem('userName');
    const userRole = localStorage.getItem('userRole');

    if (userName) {
        document.getElementById('user-display-name').textContent = userName;
    }
    if (userRole) {
        document.getElementById('user-display-role').textContent = userRole;
    }
    // --- FIM DA PARTE NOVA ---


    // --- 3. LÓGICA DO BOTÃO "SAIR" (código antigo) ---
    const logoutButton = document.getElementById('btn-sair');
    if (logoutButton) {
        logoutButton.addEventListener('click', (event) => {
            event.preventDefault(); 
            
            // Limpa TUDO do localStorage
            localStorage.clear(); 

            window.location.href = '/index.html';
        });
    }
});