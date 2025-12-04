// --- CONFIGURAÇÃO DOS SUB-MENUS ---
// Aqui definiremos as opções internas de cada módulo administrativo.
// Aguardando suas imagens para preencher corretamente.
const SUB_MENUS = {
    "COMPRAS": [
        // Exemplo: { name: "Solicitações", url: "#" }
        { name: "PEDIDO DE COMPRAS", url: "#" },
        { name: "CONTROLE DE PEDIDOS", url: "#" },
        { name: "CADASTRO DE PRODUTOS POR FORNECEDOR", url: "#" },
        { name: "CADASTRO DE FORNECEDORES", url: "#" },
        { name: "CONTROLE DE ORÇAMENTOS", url: "#" },
        { name: "CADASTRO DE PRODUTOS/SERVIÇOS", url: "#" },
        { name: "CONFIGURAÇÕES", url: "#" }
    ],
    "ESTOQUE": [],
    "VENDAS": [],
    "CONTAS A PAGAR": [],
    "CONTAS A RECEBER": [],
    // "CAD. DADOS PACIENTE" não precisa estar aqui pois é link direto
};

let MAIN_MENU_CACHE = [];

document.addEventListener('DOMContentLoaded', async () => {
    
    // 1. Verificação de Segurança
    const tipoProfi = localStorage.getItem('tipoProfi');
    if (localStorage.getItem('userIsLoggedIn') !== 'true' || tipoProfi !== '1') {
        alert("Acesso restrito ao Administrador.");
        window.location.href = '/index.html';
        return;
    }

    // 2. Exibir Nome do Usuário
    const userName = localStorage.getItem('userName');
    if (userName) document.getElementById('admin-name').textContent = userName;

    // 3. Inicializar Menu Principal
    await loadMainMenu();

    // Eventos de Clique (Voltar e Sair)
    document.getElementById('btn-back-menu').addEventListener('click', () => {
        renderMainMenu();
    });

    document.getElementById('coringa-logout').addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.clear();
        window.location.href = '/index.html';
    });
});

// --- FUNÇÕES DE LÓGICA ---

async function loadMainMenu() {
    const container = document.getElementById('admin-buttons-container');
    const tipoProfi = localStorage.getItem('tipoProfi');

    try {
        // Busca o menu raiz no Java (MenuController)
        const response = await fetch(`http://localhost:8080/api/menu?sistema=CORINGA&tipoProfi=${tipoProfi}`);
        
        if (response.ok) {
            MAIN_MENU_CACHE = await response.json();
            renderMainMenu();
        } else {
            container.innerHTML = '<p class="text-danger text-center">Erro ao carregar menu.</p>';
        }
    } catch (e) {
        console.error(e);
        container.innerHTML = '<p class="text-danger text-center">Erro de conexão.</p>';
    }
}

function renderMainMenu() {
    const container = document.getElementById('admin-buttons-container');
    const title = document.getElementById('card-title-text');
    const backBtn = document.getElementById('btn-back-menu');

    // Estado: Menu Principal
    title.textContent = "Módulos de Gestão";
    backBtn.style.display = 'none'; 
    container.innerHTML = '';

    MAIN_MENU_CACHE.forEach(item => {
        if (item.nome.startsWith('---')) {
            // Divisor
            const div = document.createElement('div');
            div.className = 'admin-divider';
            div.textContent = item.nome.replace(/---/g, '').trim();
            container.appendChild(div);
        } else {
            // Botão Principal
            const btn = document.createElement('div'); 
            btn.className = 'btn-admin-custom d-flex justify-content-between align-items-center';
            btn.innerHTML = `<span>${item.nome}</span><span>&rarr;</span>`;
            
            btn.addEventListener('click', () => {
                // Se tiver sub-menu configurado, abre ele
                if (SUB_MENUS[item.nome] && SUB_MENUS[item.nome].length > 0) {
                    renderSubMenu(item.nome);
                } else {
                    // Se não tiver sub-menu (ex: Cad. Dados Paciente), navega direto
                    console.log("Navegando para:", item.url);
                    // window.location.href = item.url; // Descomentar quando tiver URLs reais
                }
            });
            container.appendChild(btn);
        }
    });
}

function renderSubMenu(moduleName) {
    const container = document.getElementById('admin-buttons-container');
    const title = document.getElementById('card-title-text');
    const backBtn = document.getElementById('btn-back-menu');

    // Estado: Sub-Menu
    title.textContent = moduleName; // Título muda para o nome do módulo
    backBtn.style.display = 'block'; // Seta aparece
    container.innerHTML = '';

    const subItems = SUB_MENUS[moduleName];

    if (subItems && subItems.length > 0) {
        subItems.forEach(sub => {
            const btn = document.createElement('a');
            btn.href = sub.url;
            btn.className = 'btn-admin-custom';
            btn.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <span>${sub.name}</span>
                    <i class="ph ph-caret-right"></i>
                </div>
            `;
            container.appendChild(btn);
        });
    } else {
        container.innerHTML = '<p class="text-muted text-center mt-4">Nenhuma funcionalidade cadastrada.</p>';
    }
}