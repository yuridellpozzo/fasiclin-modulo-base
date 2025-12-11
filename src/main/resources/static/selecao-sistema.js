document.addEventListener('DOMContentLoaded', () => {

    const tipoProfi = localStorage.getItem('tipoProfi');
    const isSystemAdmin = localStorage.getItem('isSystemAdmin') === 'true';
    
    // --- CORREÇÃO 1: Usar o nome correto da chave (userSpecialties) ---
    // Tenta ler 'userSpecialties' (novo) ou 'userEspecialidades' (velho) para garantir
    const rawSpecialties = localStorage.getItem('userSpecialties') || localStorage.getItem('userEspecialidades');
    const userEspecialidades = JSON.parse(rawSpecialties || '[]');

    console.log("Sistemas carregados:", userEspecialidades); // Debug para você ver no F12

    // --- 1. GUARDA DE SEGURANÇA ---
    if (!isSystemAdmin && tipoProfi !== '4') {
        alert("Acesso negado. Apenas Administradores ou Masters podem acessar esta página.");
        window.location.href = '/index.html';
        return;
    }

    // --- 2. LÓGICA DOS BOTÕES ---
    const allButtons = document.querySelectorAll('.system-select-btn');

    // Se for MASTER (Tipo 4) e não for Admin Geral
    if (tipoProfi === '4' && !isSystemAdmin) {
        
        // Esconde todos primeiro
        allButtons.forEach(btn => btn.style.display = 'none');

        // Mostra apenas os permitidos
        userEspecialidades.forEach(item => {
            // --- CORREÇÃO 2: O Java manda Strings direto ("BIOMEDICINA"), não objetos ---
            // Verifica se 'item' é objeto (velho) ou string (novo)
            const systemName = (typeof item === 'string') ? item : item.nome; 
            
            if (systemName) {
                const selector = `.system-select-btn[data-system="${systemName.toUpperCase()}"]`;
                const targetBtn = document.querySelector(selector);
                
                if (targetBtn) {
                    targetBtn.style.display = 'block';
                }
            }
        });
    } 
    // Se for Admin Geral (isSystemAdmin), ele mantém todos visíveis (padrão do CSS)

    // --- 3. AÇÃO DO CLIQUE ---
    allButtons.forEach(button => {
        button.addEventListener('click', () => {
            const selectedSystem = button.getAttribute('data-system');
            localStorage.setItem('sistemaSelecionado', selectedSystem);
            window.location.href = '/pages/tela-principal.html';
        });
    });

    // --- 4. LOGOUT ---
    const logoutButton = document.getElementById('admin-logout');
    if (logoutButton) {
        logoutButton.addEventListener('click', (event) => {
            event.preventDefault();
            localStorage.clear();
            window.location.href = '/index.html';
        });
    }
});