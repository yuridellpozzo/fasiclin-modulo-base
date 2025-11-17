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

            // Fetch para a API
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
                console.log("Login sucesso:", data);
                
                // --- MUDANÇA AQUI (Passo 2): Salvar novos dados e Roteamento ---
                
                localStorage.setItem('userIsLoggedIn', 'true');
                localStorage.setItem('userName', data.nome);
                localStorage.setItem('userRole', data.cargo);
                localStorage.setItem('tipoProfi', data.tipoProfi);
                // Salva as especialidades como String JSON para usar depois
                localStorage.setItem('userEspecialidades', JSON.stringify(data.especialidades));

                // Lógica de Redirecionamento (Conforme seu Prompt)
                if (data.tipoProfi === '1') {
                    // TIPO 1 = ADMINISTRADOR -> Vai para tela Administrativa (Coringa)
                    // Obs: Precisamos criar 'admin-coringa.html' depois
                    window.location.href = "/pages/admin-coringa.html"; 

                } else if (data.tipoProfi === '4') {
                    // TIPO 4 = MASTER -> Vai para tela de Seleção de Módulo
                    window.location.href = "/pages/selecao-sistema.html"; 

                } else {
                    // TIPO 2 (Profissional) e 3 (Supervisor) -> Vai direto para o Módulo
                    window.location.href = "/pages/tela-principal.html"; 
                }
                // --- FIM DA MUDANÇA ---
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