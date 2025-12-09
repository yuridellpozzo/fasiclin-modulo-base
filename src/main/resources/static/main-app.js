// --- CONFIGURAÇÃO PADRÃO ---
const DEFAULT_THEME = {
    colorPrimary: "#000000", colorSecondary: "#198754", colorText: "#ffffff", fontFamily: "'Poppins', sans-serif"
};

const translations = {
    "pt": { "menu_principal": "MENU PRINCIPAL", "voltar_sistemas": "Voltar para Sistemas", "sair": "Sair" },
    "en": { "menu_principal": "MAIN MENU", "voltar_sistemas": "Back to Systems", "sair": "Logout" },
    "es": { "menu_principal": "MENÚ PRINCIPAL", "voltar_sistemas": "Volver a Sistemas", "sair": "Salir" }
};

// --- APLICA TEMA ---
async function applyTheme(sistema) {
    try {
        const response = await fetch(`http://localhost:8080/api/config/${sistema}?t=${new Date().getTime()}`);
        const defaultLogoUrl = getDefaultLogoUrl(sistema);

        if (response.ok && response.status !== 204) {
            const config = await response.json();
            const finalTheme = { ...DEFAULT_THEME, ...config, logoBase64: config.logoBase64 || defaultLogoUrl };
            renderTheme(finalTheme);
            localStorage.setItem('appLanguage', config.language || 'pt');
            applyLanguage(config.language || 'pt');
        } else {
            const themePadrao = { ...DEFAULT_THEME, logoBase64: defaultLogoUrl };
            renderTheme(themePadrao);
            localStorage.setItem('appLanguage', 'pt');
            applyLanguage('pt');
        }
    } catch (error) {
        console.error("Erro tema:", error);
        renderTheme({ ...DEFAULT_THEME, logoBase64: getDefaultLogoUrl(sistema) });
    }
}

function renderTheme(config) {
    const logoElement = document.getElementById('logo-sistema');
    if (config.logoBase64 && logoElement) logoElement.src = config.logoBase64;

    const mainNavbar = document.getElementById('main-navbar');
    const mainOffcanvas = document.getElementById('main-offcanvas');

    if (mainNavbar) {
        mainNavbar.classList.remove('bg-dark', 'navbar-dark');
        mainNavbar.style.cssText = `background-color: ${config.colorPrimary} !important; transition: 0.3s;`;
    }
    if (mainOffcanvas) {
        mainOffcanvas.classList.remove('text-bg-dark', 'bg-dark');
        mainOffcanvas.style.cssText = `background-color: ${config.colorPrimary} !important; color: ${config.colorText} !important; transition: 0.3s;`;
    }

    let dynamicStyles = ``;
    if (config.colorSecondary) {
        dynamicStyles += `
            .navbar-toggler { background-color: ${config.colorSecondary} !important; border-color: rgba(255,255,255,0.5) !important; }
            .offcanvas-body .btn-success { background-color: ${config.colorSecondary} !important; border-color: ${config.colorSecondary} !important; color: #ffffff !important; }
            .offcanvas-body .btn-success:hover { background-color: #ffffff !important; color: ${config.colorSecondary} !important; border-color: ${config.colorSecondary} !important; }
        `;
    }
    if (config.colorText) {
        dynamicStyles += ` body, h1, h2, h3, h4, h5, h6, span, small, .offcanvas-title, .nav-link, #user-display-name, #user-display-role { color: ${config.colorText} !important; } .menu-divisor { color: ${config.colorText} !important; opacity: 0.8; font-weight: 700; display: block; margin-top: 10px; } `;
    }
    if (config.fontFamily) {
        dynamicStyles += ` body, h1, h2, h3, h4, h5, h6, p, span, a, button, input, .btn { font-family: ${config.fontFamily} !important; font-size: ${config.fontSize || '16px'} !important; } `;
    }

    const oldStyle = document.getElementById('dynamic-theme-style');
    if (oldStyle) oldStyle.remove();
    const styleTag = document.createElement('style');
    styleTag.id = 'dynamic-theme-style';
    styleTag.innerHTML = dynamicStyles;
    document.head.appendChild(styleTag);
}

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
    const menuTitle = document.getElementById('offcanvasDarkNavbarLabel');
    if (menuTitle) menuTitle.textContent = dict["menu_principal"];
    const btnVoltar = document.getElementById('btn-voltar-selecao');
    if (btnVoltar) btnVoltar.innerHTML = `&laquo; ${dict["voltar_sistemas"]}`;
    const btnSair = document.getElementById('btn-sair');
    if (btnSair) btnSair.textContent = dict["sair"];
}

// --- CARREGAMENTO DO MENU COM IFRAME ---
async function loadDynamicMenu(sistema, tipoProfi) {
    const menuContainer = document.getElementById('menu-container');
    if (!menuContainer) return; 
    try {
        const response = await fetch(`http://localhost:8080/api/menu?sistema=${sistema}&tipoProfi=${tipoProfi}`);
        if (!response.ok) throw new Error("Status: " + response.status);
        const menuItems = await response.json();
        
        menuContainer.innerHTML = '';
        if (menuItems.length === 0) menuContainer.innerHTML = '<span class="text-white small ms-3">Menu vazio.</span>';

        const currentLang = localStorage.getItem('appLanguage') || 'pt';
        const dict = translations[currentLang];

        menuItems.forEach(item => {
            let displayText = item.nome;
            if (item.nome.startsWith('---') && dict && dict[item.nome]) {
                displayText = dict[item.nome];
            }

            // --- AQUI ESTÁ A LÓGICA DO IFRAME ---
            const newButton = document.createElement('a');
            newButton.className = 'btn btn-success text-start'; 
            newButton.role = 'button';
            newButton.textContent = displayText;

            if (item.nome.startsWith('---')) {
                newButton.className = 'menu-divisor text-uppercase small mt-2 border-0 bg-transparent ps-0';
                newButton.textContent = displayText.replace(/---/g, '').trim(); 
                newButton.removeAttribute('href');
            } else {
                // Se tiver URL válida, aponta para o iframe
                if (item.url && item.url !== '#') {
                    newButton.href = item.url;
                    newButton.target = "content-frame"; // NOME DO IFRAME NO HTML
                    
                    // Esconde a mensagem de boas vindas ao clicar
                    newButton.addEventListener('click', () => {
                        const welcome = document.getElementById('welcome-message');
                        if (welcome) welcome.style.display = 'none';
                        
                        // Fecha o menu lateral em telas pequenas (UX melhor)
                        const offcanvasEl = document.getElementById('main-offcanvas');
                        const bsOffcanvas = bootstrap.Offcanvas.getInstance(offcanvasEl);
                        if (bsOffcanvas) bsOffcanvas.hide();
                    });
                } else {
                    newButton.href = "#";
                }
            }
            menuContainer.appendChild(newButton);
        });
    } catch (error) { console.error("Erro menu:", error); }
}

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
    
    let sistemaParaCarregar = localStorage.getItem('sistemaSelecionado') || localStorage.getItem('userSystem') || "BIOMEDICINA";
    
    if (sistemaParaCarregar && tipoProfi) {
        applyTheme(sistemaParaCarregar).then(() => { loadDynamicMenu(sistemaParaCarregar, tipoProfi); });
    }
});