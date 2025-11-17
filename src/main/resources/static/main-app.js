// --- FUNÇÃO 1: CARREGAR O MENU DINÂMICO (CORRIGIDA) ---
// Agora aceita 'tipoProfi' (ex: "4") em vez de 'cargo' (ex: "Master")
async function loadDynamicMenu(sistema, tipoProfi) {
    const menuContainer = document.getElementById('menu-container');
    if (!menuContainer) return; 

    try {
        // --- MUDANÇA CRÍTICA AQUI ---
        // O endpoint agora espera 'tipoProfi', não 'cargo'
        const response = await fetch(`http://localhost:8080/api/menu?sistema=${sistema}&tipoProfi=${tipoProfi}`);
        
        if (!response.ok) {
            throw new Error("Não foi possível carregar o menu. Status: " + response.status);
        }

        const menuItems = await response.json();

        // Limpa o container antes de adicionar (para evitar duplicatas)
        menuContainer.innerHTML = '';

        // 2. Constrói os botões
        menuItems.forEach(item => {
            const newButton = document.createElement('a');
            newButton.href = item.url;
            newButton.className = 'btn btn-success text-start'; 
            newButton.role = 'button';
            newButton.textContent = item.nome;
            
            // Estilo especial para divisores
            if (item.nome.startsWith('---')) {
                newButton.className = 'text-white text-uppercase small disabled mt-2';
                newButton.textContent = item.nome.replace('---', ''); 
                // Remove o href para divisores
                newButton.removeAttribute('href');
            }
            
            menuContainer.appendChild(newButton);
        });

    } catch (error) {
        console.error("Erro ao carregar menu:", error);
        menuContainer.innerHTML = '<span class="text-danger">Erro ao carregar o menu.</span>';
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

    // Se for Admin Geral OU Master (Tipo 4), mostramos o botão de voltar
    const tipoProfi = localStorage.getItem('tipoProfi');
    
    if ((isAdmin === 'true' || tipoProfi === '4') && btnVoltar) {
        btnVoltar.style.display = 'block';
        if(adminDivider) adminDivider.style.display = 'block';
        
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

    // --- 5. CARREGAR O MENU (CORRIGIDO) ---
    
    // Tenta pegar do Admin/Master (seleção) OU do usuário comum (login)
    let sistemaParaCarregar = localStorage.getItem('sistemaSelecionado');
    
    // Se não houver seleção (Tipos 2 e 3), pega do login
    if (!sistemaParaCarregar) {
        sistemaParaCarregar = localStorage.getItem('userSystem');
    }

    console.log("Sistema:", sistemaParaCarregar); 
    console.log("Tipo Profi:", tipoProfi);       

    // Verifica se temos o sistema E o tipoProfi
    if (sistemaParaCarregar && tipoProfi) {
        
        // --- MUDANÇA CRÍTICA AQUI ---
        // Chama a função passando 'tipoProfi' em vez de 'userRole'
        loadDynamicMenu(sistemaParaCarregar, tipoProfi);
        
        // Carrega a Logo
        carregarLogo(sistemaParaCarregar);

    } else {
        console.error("Dados insuficientes. Sistema ou Tipo Profissional faltando.");
        console.error("Sistema:", sistemaParaCarregar, "Tipo:", tipoProfi);
    }
});

// Função separada para carregar a logo
function carregarLogo(sistema) {
    const logoElement = document.getElementById('logo-sistema');
    if (logoElement && sistema) {
        // Normaliza para maiúsculas para evitar erros de case
        const sys = sistema.toUpperCase();
        
        switch (sys) {
            case 'BIOMEDICINA':
                logoElement.src = '/img/icones-pequenos-topo-bio.jpg';
                break;
            case 'ODONTOLOGIA':
                // Certifique-se que este arquivo existe na pasta /img/
                logoElement.src = '/img/icones-peq-topo-odonto.png'; 
                break;
            case 'NUTRICAO':
                logoElement.src = '/img/logo-nutricao.jpg';
                break;
            case 'PSICOLOGIA':
                logoElement.src = '/img/logo-psicologia.jpg';
                break;
            case 'FISIOTERAPIA':
                logoElement.src = '/img/logo-fisioterapia.jpg';
                break;
            default:
                // Se não achar, usa uma logo padrão ou a de Biomedicina como fallback
                logoElement.src = '/img/icones-pequenos-topo-bio.jpg'; 
                console.warn("Logo não definida para o sistema:", sys);
        }
    }
}