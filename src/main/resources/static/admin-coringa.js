// --- CONFIGURAÇÃO DOS SUB-MENUS ---
const SUB_MENUS = {
    "COMPRAS": [
        { name: "PEDIDO DE COMPRAS", url: "#" },
        { name: "CONTROLE DE PEDIDOS", url: "#" },
        { name: "CAD. FORNECEDORES", url: "#" },
        { name: "CONFIGURAÇÕES", url: "#" }
    ],
    "ESTOQUE": [
        { name: "REL. PEDIDOS EM ABERTO", url: "#" },
        { name: "BAIXA DE ESTOQUE", url: "#" },
        { name: "CONFIGURAÇÕES", url: "#" }
    ],
    "VENDAS": [
        { name: "EMISSÃO DE RECIBO / NF", url: "#" },
        { name: "REL. VENDAS", url: "#" }
    ],
    "CONTAS A PAGAR": [
        { name: "CADASTRO DE PAGAMENTOS", url: "#" },
        { name: "RELATÓRIOS", url: "#" }
    ],
    "CONTAS A RECEBER": [
        { name: "CAD. RECEBÍVEIS", url: "#" },
        { name: "CONTROLE DE COBRANÇA", url: "#" }
    ]
};

let MAIN_MENU_CACHE = [];

document.addEventListener('DOMContentLoaded', async () => {
    try {
        const tipoProfi = localStorage.getItem('tipoProfi');
        if (tipoProfi !== '1') return; 

        const userName = localStorage.getItem('userName');
        if (document.getElementById('admin-name') && userName) {
            document.getElementById('admin-name').textContent = userName;
        }

        await loadMainMenu();

        // Botão Voltar
        const btnBack = document.getElementById('btn-back-menu');
        if (btnBack) {
            btnBack.addEventListener('click', () => renderMainMenu());
        }

        // Logout
        const btnLogout = document.getElementById('coringa-logout');
        if (btnLogout) {
            btnLogout.addEventListener('click', (e) => {
                e.preventDefault();
                localStorage.clear();
                window.location.href = '/index.html';
            });
        }
    } catch (error) { console.error(error); }
});

async function loadMainMenu() {
    const container = document.getElementById('admin-buttons-container');
    if(!container) return;

    try {
        const tipoProfi = localStorage.getItem('tipoProfi');
        const response = await fetch(`http://localhost:8080/api/menu?sistema=CORINGA&tipoProfi=${tipoProfi}`);
        if (response.ok) {
            MAIN_MENU_CACHE = await response.json();
            renderMainMenu();
        }
    } catch (e) {
        container.innerHTML = '<p class="text-danger text-center">Erro de conexão.</p>';
    }
}

function renderMainMenu() {
    const container = document.getElementById('admin-buttons-container');
    const title = document.getElementById('card-title-text');
    const backBtn = document.getElementById('btn-back-menu');

    if (title) title.textContent = "Módulos de Gestão";
    if (backBtn) backBtn.style.display = 'none';
    container.innerHTML = '';

    MAIN_MENU_CACHE.forEach(item => {
        if (item.nome.startsWith('---')) {
            const div = document.createElement('div');
            div.className = 'admin-divider fw-bold mt-3 mb-2 text-uppercase text-muted';
            div.textContent = item.nome.replace(/---/g, '').trim();
            container.appendChild(div);
        } else {
            // --- AQUI ESTÁ A CORREÇÃO DA COR (VERDE COM TEXTO BRANCO) ---
            const btn = document.createElement('div');
            // Usando classes do Bootstrap: bg-success (Verde) e text-white (Branco)
            btn.className = 'btn-admin-custom d-flex justify-content-between align-items-center p-3 mb-2 bg-success text-white border rounded shadow-sm';
            btn.style.cursor = 'pointer';
            
            btn.innerHTML = `<span class="fw-bold">${item.nome}</span><span>&rarr;</span>`;
            
            btn.addEventListener('click', () => {
                if (SUB_MENUS[item.nome]) {
                    renderSubMenu(item.nome);
                } else {
                    abrirTelaNoCard(item.nome, item.url);
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

    if (title) title.textContent = moduleName;
    if (backBtn) backBtn.style.display = 'block';
    container.innerHTML = '';

    const subItems = SUB_MENUS[moduleName];
    if (subItems) {
        subItems.forEach(sub => {
            const btn = document.createElement('a');
            btn.href = sub.url;
            // Sub-itens também verdes para manter o padrão
            btn.className = 'd-block p-3 mb-2 bg-success text-white border rounded text-decoration-none shadow-sm';
            btn.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <span>${sub.name}</span>
                    <small>Acessar</small>
                </div>
            `;
            container.appendChild(btn);
        });
    }
}

function abrirTelaNoCard(titulo, url) {
    const container = document.getElementById('admin-buttons-container');
    const title = document.getElementById('card-title-text');
    const backBtn = document.getElementById('btn-back-menu');

    if (title) title.textContent = titulo;
    if (backBtn) backBtn.style.display = 'block';
    container.innerHTML = ''; 

    const iframe = document.createElement('iframe');
    iframe.src = url;
    iframe.style.width = "100%";
    iframe.style.height = "600px";
    iframe.style.border = "none";
    container.appendChild(iframe);
}