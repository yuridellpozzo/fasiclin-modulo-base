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

            // Faz a requisição para o Java
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
                console.log("--- LOGIN SUCESSO ---");
                console.log(data); // Para você conferir no F12

                // 1. LIMPA DADOS ANTIGOS
                localStorage.clear();
                
                // 2. SALVA OS NOVOS DADOS NO NAVEGADOR
                localStorage.setItem('userIsLoggedIn', 'true');
                localStorage.setItem('userName', data.nome);
                localStorage.setItem('userRole', data.cargo);
                localStorage.setItem('tipoProfi', data.tipoProfi);
                localStorage.setItem('isSystemAdmin', data.isSystemAdmin);
                
                // IMPORTANTE: Salva o sistema padrão calculado pelo Java (ex: "ODONTOLOGIA")
                // Isso resolve o problema de quem não passa pela tela de seleção
                localStorage.setItem('userSystem', data.sistema); 
                
                localStorage.setItem('userEspecialidades', JSON.stringify(data.especialidades));

                // 3. ROTEAMENTO (Para onde cada um vai)
                if (data.tipoProfi === '1') {
                    // Admin -> Tela Coringa
                    window.location.href = "/pages/admin-coringa.html"; 
                } else if (data.tipoProfi === '4') {
                    // Master -> Tela de Seleção
                    window.location.href = "/pages/selecao-sistema.html"; 
                } else {
                    // Tipos 2 e 3 (Profissional/Supervisor) -> Direto para o sistema
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