document.addEventListener("DOMContentLoaded", () => {
    
    const loginForm = document.getElementById("login-form");
    const usernameInput = document.getElementById("inputUsuario"); 
    const passwordInput = document.getElementById("inputSenha"); 
    const errorMessage = document.getElementById("error-message");

    if (loginForm) {
        loginForm.addEventListener("submit", (event) => {
            event.preventDefault(); 

            const loginData = {
                login: usernameInput.value, 
                senha: passwordInput.value 
            };

            errorMessage.textContent = "";

            fetch("http://localhost:8080/api/auth/login", { 
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(loginData) 
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Falha no login');
                }
            })
            .then(data => {
                console.log("--- LOGIN SUCESSO ---", data);

                // 1. LIMPA DADOS ANTIGOS
                localStorage.clear();
                
                // 2. SALVA OS NOVOS DADOS
                localStorage.setItem('userIsLoggedIn', 'true');
                localStorage.setItem('userName', data.nome);
                localStorage.setItem('userRole', data.cargo);
                localStorage.setItem('tipoProfi', data.tipoProfi);
                localStorage.setItem('isSystemAdmin', data.isSystemAdmin);
                localStorage.setItem('userSystem', data.sistema); 
                localStorage.setItem('userEspecialidades', JSON.stringify(data.especialidades));

                // 3. ROTEAMENTO (AJUSTADO CONFORME SEU PEDIDO)
                if (data.tipoProfi === '1') {
                    // TIPO 1 (ADMIN) -> Vai para o PAINEL ADMINISTRATIVO Próprio
                    window.location.href = "/pages/admin-coringa.html"; 
                
                } else if (data.tipoProfi === '4') {
                    // TIPO 4 (MASTER) -> Vai para Seleção
                    window.location.href = "/pages/selecao-sistema.html"; 
                
                } else {
                    // TIPOS 2 e 3 (SAÚDE) -> Vão para a Tela Principal
                    window.location.href = "/pages/tela-principal.html"; 
                }
            })
            .catch(error => {
                console.error("Erro:", error);
                if (error.message.includes('Failed to fetch')) {
                    errorMessage.textContent = "Não foi possível conectar ao servidor.";
                } else {
                    errorMessage.textContent = "Usuário ou senha inválidos.";
                }
            });
        });
    }
});