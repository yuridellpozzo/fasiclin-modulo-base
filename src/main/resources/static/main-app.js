// --- FUNÇÃO 1: CARREGAR O MENU DINÂMICO ---
async function loadDynamicMenu(sistema, cargo) {
    const menuContainer = document.getElementById('menu-container');
    if (!menuContainer) return; // Sai se o contêiner não existir

    try {
        // 1. Busca no back-end o menu correto
        const response = await fetch(`http://localhost:8080/api/menu?sistema=${sistema}&cargo=${cargo}`);
        
        if (!response.ok) {
            throw new Error("Não foi possível carregar o menu.");
        }

        const menuItems = await response.json();

        // 2. Constrói os botões
        menuItems.forEach(item => {
            const newButton = document.createElement('a');
            newButton.href = item.url;
            newButton.className = 'btn btn-success text-start'; // Usa o estilo dos seus botões antigos
            newButton.role = 'button';
            newButton.textContent = item.nome;
            
            // Adiciona botões especiais (como o divisor) com um estilo diferente
            if (item.nome.startsWith('---')) {
                newButton.className = 'text-white text-uppercase small disabled mt-2';
                newButton.textContent = item.nome.replace('---', ''); // Remove os traços
            }
            
            menuContainer.appendChild(newButton);
        });

    } catch (error) {
        console.error("Erro ao carregar menu:", error);
        menuContainer.innerHTML += '<span class="text-danger">Erro ao carregar o menu.</span>';
    }
}


// --- FUNÇÃO 2: PONTO DE ENTRADA (QUANDO A PÁGINA CARREGA) ---
document.addEventListener('DOMContentLoaded', () => {
    
    // --- 1. PROTEÇÃO DA PÁGINA ---
    if (localStorage.getItem('userIsLoggedIn') !== 'true') {
        alert("Acesso negado. Por favor, faça o login.");
        window.location.href = '/index.html'; 
        return; 
    }

    // --- 2. LÓGICA DO ADMINISTRADOR (Botão Voltar) ---
    const isAdmin = localStorage.getItem('isSystemAdmin');
    const btnVoltar = document.getElementById('btn-voltar-selecao');
    const adminDivider = document.getElementById('admin-divider');

    if (isAdmin === 'true' && btnVoltar) {
        btnVoltar.style.display = 'block';
        adminDivider.style.display = 'block';
        btnVoltar.addEventListener('click', (e) => {
            e.preventDefault(); 
            window.location.href = '/pages/selecao-sistema.html';
        });
    }

    // --- 3. PREENCHER DADOS DO USUÁRIO (Nome e Cargo) ---
    const userName = localStorage.getItem('userName');
    const userRole = localStorage.getItem('userRole');

    if (userName) {
        document.getElementById('user-display-name').textContent = userName;
    }
    if (userRole) {
        document.getElementById('user-display-role').textContent = userRole;
    }

    // --- 4. LÓGICA DO BOTÃO "SAIR" ---
    const logoutButton = document.getElementById('btn-sair');
    if (logoutButton) {
        logoutButton.addEventListener('click', (event) => {
            event.preventDefault(); 
            localStorage.clear(); 
            window.location.href = '/index.html';
        });
    }

    // --- 5. CARREGAR O MENU (A GRANDE MUDANÇA) ---
    
    // Decide qual sistema deve ser carregado
    // Se o Admin escolheu um, usa esse. Senão, usa o do próprio usuário.
    const sistemaParaCarregar = localStorage.getItem('sistemaSelecionado') || localStorage.getItem('userSystem');
    const cargoDoUsuario = localStorage.getItem('userRole');

    if (sistemaParaCarregar && cargoDoUsuario) {
        loadDynamicMenu(sistemaParaCarregar, cargoDoUsuario);
    } else {
        console.error("Não foi possível determinar o sistema ou cargo do usuário.");
    }
});

const sistema = localStorage.getItem('sistemaSelecionado') || localStorage.getItem('userSystem');
const logoElement = document.getElementById('logo-sistema');

    if (logoElement) {
        switch (sistema) {
            case 'BIOMEDICINA':
                logoElement.src = '/img/icones-pequenos-topo-bio.jpg';
                break;
            case 'ODONTOLOGIA':
                logoElement.src = '/img/icones-peq-topo-odonto.png';
                break;
            case 'NUTRICAO':
                logoElement.src = '/img/logo-nutricao.jpg';
                break;
            // etc...
            default:
                logoElement.src = '/img/logo-padrao.jpg';
        }
    }