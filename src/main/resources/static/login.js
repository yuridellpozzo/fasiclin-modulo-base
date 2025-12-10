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
                if (response.ok) return response.json();
                throw new Error('Falha no login');
            })
            .then(data => {
                console.log("--- LOGIN SUCESSO ---", data);

                // 1. LIMPEZA
                localStorage.clear();
                
                // 2. SALVAR DADOS BÁSICOS
                localStorage.setItem('userIsLoggedIn', 'true');
                if (data.token) localStorage.setItem('userToken', data.token); 
                localStorage.setItem('userName', data.nome);
                localStorage.setItem('userRole', data.cargo);
                localStorage.setItem('tipoProfi', data.tipoProfi);
                localStorage.setItem('userSystem', data.sistema); 

                // --- NOVO: SALVAR ESPECIALIDADES (PARA OS BOTÕES APARECEREM) ---
                if (data.especialidades) {
                    localStorage.setItem('userSpecialties', JSON.stringify(data.especialidades));
                }
                // -------------------------------------------------------------

                // 3. SALVAR O ID DO PROFISSIONAL
                if (data.idProfissional) {
                    localStorage.setItem('idProfissional', data.idProfissional);
                    console.log("✅ ID GRAVADO NA MEMÓRIA:", data.idProfissional);
                } else {
                    console.warn("⚠️ ALERTA: O servidor não retornou o idProfissional!");
                }

                // 4. REDIRECIONAR
                if (data.tipoProfi === '1') {
                    window.location.href = "/pages/admin-coringa.html"; 
                } else if (data.tipoProfi === '4') {
                    window.location.href = "/pages/selecao-sistema.html"; // Agora os botões vão aparecer!
                } else {
                    window.location.href = "/pages/tela-principal.html"; 
                }
            })
            .catch(error => {
                console.error("Erro:", error);
                errorMessage.textContent = "Usuário ou senha inválidos.";
            });
        });
    }
});