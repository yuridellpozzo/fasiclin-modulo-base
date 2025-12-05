// --- CONFIGURAÇÃO PADRÃO (PRETO E VERDE) ---
const DEFAULT_THEME = {
    colorPrimary: "#000000",      // Fundo do Topo e Menu (Preto)
    colorSecondary: "#198754",    // Botões e Hambúrguer (Verde)
    colorText: "#ffffff",         // Textos (Branco)
    fontFamily: "'Poppins', sans-serif"
};

// --- TRADUÇÕES (Incluindo PT para garantir o retorno ao padrão) ---
const translations = {
    "pt": { 
        "menu_principal": "MENU PRINCIPAL", 
        "voltar_sistemas": "Voltar para Sistemas", 
        "sair": "Sair", 
        "--- ESPECÍFICO ---": "--- ESPECÍFICO ---", 
        "--- ADMINISTRAÇÃO ---": "--- ADMINISTRAÇÃO ---",
        "--- GESTÃO ---": "--- GESTÃO ---"
    },
    "en": { 
        "menu_principal": "MAIN MENU", 
        "voltar_sistemas": "Back to Systems", 
        "sair": "Logout", 
        "--- ESPECÍFICO ---": "--- SPECIFIC ---", 
        "--- ADMINISTRAÇÃO ---": "--- ADMINISTRATION ---",
        "--- GESTÃO ---": "--- MANAGEMENT ---"
    },
    "es": { 
        "menu_principal": "MENÚ PRINCIPAL", 
        "voltar_sistemas": "Volver a Sistemas", 
        "sair": "Salir", 
        "--- ESPECÍFICO ---": "--- ESPECÍFICO ---", 
        "--- ADMINISTRAÇÃO ---": "--- ADMINISTRACIÓN ---",
        "--- GESTÃO ---": "--- GESTIÓN ---"
    }
};

// --- 1. BUSCAR E APLICAR TEMA ---
async function applyTheme(sistema) {
    try {
        const response = await fetch(`http://localhost:8080/api/config/${sistema}?t=${new Date().getTime()}`);
        const defaultLogoUrl = getDefaultLogoUrl(sistema);

        if (response.ok && response.status !== 204) {
            // --- TEMA PERSONALIZADO ENCONTRADO ---
            const config = await response.json();
            console.log("🎨 TEMA PERSONALIZADO:", config);
            
            const finalTheme = {
                ...DEFAULT_THEME, 
                ...config,        
                logoBase64: config.logoBase64 || defaultLogoUrl 
            };
            
            renderTheme(finalTheme);

            // Aplica o idioma (ou volta para PT se não tiver)
            if (config.language) {
                applyLanguage(config.language);
                localStorage.setItem('appLanguage', config.language);
            } else {
                applyLanguage('pt');
                localStorage.setItem('appLanguage', 'pt');
            }

        } else {
            // --- NENHUMA CONFIGURAÇÃO -> APLICA O PADRÃO ---
            console.log("Usando TEMA PADRÃO (Preto/Verde).");
            const themePadrao = {
                ...DEFAULT_THEME,
                logoBase64: defaultLogoUrl
            };
            renderTheme(themePadrao);
            
            // Força Português no padrão
            applyLanguage('pt');
            localStorage.setItem('appLanguage', 'pt');
        }

    } catch (error) {
        console.error("Erro ao aplicar tema:", error);
        // Fallback de segurança
        const themePadrao = {
            ...DEFAULT_THEME,
            logoBase64: getDefaultLogoUrl(sistema)
        };
        renderTheme(themePadrao);
        applyLanguage('pt');
        localStorage.setItem('appLanguage', 'pt');
    }
}

// --- FUNÇÃO DE PINTURA (RENDERIZAR O CSS) ---
function renderTheme(config) {
    // 1. LOGO
    const logoElement = document.getElementById('logo-sistema');
    if (config.logoBase64 && logoElement) {
        logoElement.src = config.logoBase64;
    }

    // 2. COR PRIMÁRIA (Topo e Menu)
    const mainNavbar = document.getElementById('main-navbar');
    const mainOffcanvas = document.getElementById('main-offcanvas');

    if (mainNavbar) {
        mainNavbar.classList.remove('bg-dark', 'navbar-dark');
        mainNavbar.style.cssText = `background-color: ${config.colorPrimary} !important; transition: 0.3s;`;
    }
    
    if (mainOffcanvas) {
        mainOffcanvas.classList.remove('text-bg-dark', 'bg-dark');
        // Aplica fundo e cor do texto base
        mainOffcanvas.style.cssText = `background-color: ${config.colorPrimary} !important; color: ${config.colorText} !important; transition: 0.3s;`;
    }

    // 3. CSS DINÂMICO (Estilos detalhados)
    let dynamicStyles = ``;

    // Botões e Hambúrguer
    if (config.colorSecondary) {
        dynamicStyles += `
            /* Botão Hambúrguer (Fundo colorido) */
            .navbar-toggler {
                background-color: ${config.colorSecondary} !important;
                border-color: rgba(255,255,255,0.5) !important;
            }
            /* Botões do Menu (Normal: Fundo Cor Secundária, Texto Branco) */
            .offcanvas-body .btn-success { 
                background-color: ${config.colorSecondary} !important; 
                border-color: ${config.colorSecondary} !important;
                color: #ffffff !important;
            }
            /* Botões do Menu (Hover: Fundo Branco, Texto Cor Secundária) */
            .offcanvas-body .btn-success:hover {
                background-color: #ffffff !important;
                color: ${config.colorSecondary} !important;
                border-color: ${config.colorSecondary} !important;
                filter: brightness(100%);
            }
        `;
    }

    // Textos Específicos
    if (config.colorText) {
        dynamicStyles += `
            /* Aplica a cor do texto em títulos e links */
            body, h1, h2, h3, h4, h5, h6, span, small, .offcanvas-title, .nav-link, #user-display-name, #user-display-role {
                color: ${config.colorText} !important;
            }
            /* Divisores (ESPECÍFICO, ADMINISTRAÇÃO) - Sem sublinhado */
            .menu-divisor {
                color: ${config.colorText} !important;
                opacity: 0.8;
                text-decoration: none !important;
                font-weight: 700;
                display: block; /* Garante que ocupe a linha */
                margin-top: 10px;
            }
        `;
    }

    // Fonte e Tamanho
    if (config.fontFamily || config.fontSize) {
        const font = config.fontFamily ? `font-family: ${config.fontFamily} !important;` : '';
        const size = config.fontSize ? `font-size: ${config.fontSize} !important;` : '';
        dynamicStyles += ` body, h1, h2, h3, h4, h5, h6, p, span, a, button, input, .btn { ${font} ${size} } `;
    }

    // Injeta o CSS na página
    const oldStyle = document.getElementById('dynamic-theme-style');
    if (oldStyle) oldStyle.remove();
    const styleTag = document.createElement('style');
    styleTag.id = 'dynamic-theme-style';
    styleTag.innerHTML = dynamicStyles;
    document.head.appendChild(styleTag);
}

// --- AUXILIARES ---
function getDefaultLogoUrl(sistema) {
    const sys = sistema ? sistema.toUpperCase() : "";
    switch (sys) {
        case 'BIOMEDICINA': return '/img/icones-peq-topo-bio.jpg';
        case 'ODONTOLOGIA': return '/img/icones-peq-topo-odonto.png';
        case 'NUTRICAO': return '/img/icones-peq-topo-nutri.avif';
        case 'PSICOLOGIA': return '/img/icones-peq-topo-psico.avif';
        case 'FISIOTERAPIA': return '/img/icones-peq-topo-fisio.jpg';
        default: return '/img/icones-peq-topo-bio.jpg';
    }
}

function applyLanguage(lang) {
    const dict = translations[lang];
    if (!dict) return;

    // Traduz elementos fixos da tela
    const menuTitle = document.getElementById('offcanvasDarkNavbarLabel');
    if (menuTitle) menuTitle.textContent = dict["menu_principal"];
    
    const btnVoltar = document.getElementById('btn-voltar-selecao');
    if (btnVoltar) btnVoltar.innerHTML = `&laquo; ${dict["voltar_sistemas"]}`;
    
    const btnSair = document.getElementById('btn-sair');
    if (btnSair) btnSair.textContent = dict["sair"];
}

async function loadDynamicMenu(sistema, tipoProfi) {
    const menuContainer = document.getElementById('menu-container');
    if (!menuContainer) return; 
    try {
        const response = await fetch(`http://localhost:8080/api/menu?sistema=${sistema}&tipoProfi=${tipoProfi}`);
        if (!response.ok) throw new Error("Status: " + response.status);
        const menuItems = await response.json();
        
        menuContainer.innerHTML = '';
        if (menuItems.length === 0) menuContainer.innerHTML = '<span class="text-white small ms-3">Menu vazio.</span>';

        // Usa o idioma salvo para traduzir os itens do menu
        const currentLang = localStorage.getItem('appLanguage') || 'pt';
        const dict = translations[currentLang];

        menuItems.forEach(item => {
            let displayText = item.nome;
            
            // Traduz os divisores se houver tradução disponível
            if (item.nome.startsWith('---') && dict && dict[item.nome]) {
                displayText = dict[item.nome];
            }

            const newButton = document.createElement('a');
            newButton.href = item.url;
            newButton.className = 'btn btn-success text-start'; 
            newButton.role = 'button';
            newButton.textContent = displayText;
            
            if (item.nome.startsWith('---')) {
                // Aplica estilo de divisor (sem link, sem sublinhado)
                newButton.className = 'menu-divisor text-uppercase small mt-2 border-0 bg-transparent ps-0';
                // Remove os traços visualmente se desejar, ou mantém
                newButton.textContent = displayText.replace(/---/g, '').trim(); 
                newButton.removeAttribute('href');
                newButton.style.pointerEvents = 'none'; // Não clicável
            }
            menuContainer.appendChild(newButton);
        });
    } catch (error) { console.error("Erro menu:", error); }
}

// --- INICIALIZAÇÃO ---
document.addEventListener('DOMContentLoaded', () => {
    if (localStorage.getItem('userIsLoggedIn') !== 'true') { window.location.href = '/index.html'; return; }
    
    const userName = localStorage.getItem('userName');
    const userRole = localStorage.getItem('userRole');
    if (userName) document.getElementById('user-display-name').textContent = userName;
    if (userRole) document.getElementById('user-display-role').textContent = userRole;

    const isAdmin = localStorage.getItem('isSystemAdmin');
    const tipoProfi = localStorage.getItem('tipoProfi');
    const btnVoltar = document.getElementById('btn-voltar-selecao');
    const adminDivider = document.getElementById('admin-divider');

    if ((isAdmin === 'true' || tipoProfi === '4') && btnVoltar) {
        btnVoltar.style.display = 'block';
        if(adminDivider) adminDivider.style.display = 'block';
        btnVoltar.addEventListener('click', (e) => { e.preventDefault(); window.location.href = '/pages/selecao-sistema.html'; });
    }
    
    const logoutButton = document.getElementById('btn-sair');
    if (logoutButton) {
        logoutButton.addEventListener('click', (e) => { e.preventDefault(); localStorage.clear(); window.location.href = '/index.html'; });
    }
    
    let sistemaParaCarregar = localStorage.getItem('sistemaSelecionado');
    if (!sistemaParaCarregar || sistemaParaCarregar === 'null') sistemaParaCarregar = localStorage.getItem('userSystem');
    if (!sistemaParaCarregar || sistemaParaCarregar === 'null' || sistemaParaCarregar === 'undefined' || sistemaParaCarregar === 'INDEFINIDO') {
        sistemaParaCarregar = "BIOMEDICINA";
    }

    if (sistemaParaCarregar && tipoProfi) {
        // Executa applyTheme primeiro para definir o idioma, depois carrega o menu
        applyTheme(sistemaParaCarregar).then(() => { loadDynamicMenu(sistemaParaCarregar, tipoProfi); });
    }
});