document.addEventListener("DOMContentLoaded", () => {
    
    // 1. Pega os elementos pelos IDs corretos do seu HTML
    const loginForm = document.getElementById("login-form");
    const usernameInput = document.getElementById("inputUsuario"); 
    const passwordInput = document.getElementById("inputSenha"); 
    const errorMessage = document.getElementById("error-message");

    // Verifica se o formulário existe na página
    if (loginForm) {
        
        loginForm.addEventListener("submit", (event) => {
            
            // Impede o formulário de recarregar a página
            event.preventDefault(); 

            // 2. Pega os valores dos seus inputs
            const username = usernameInput.value;
            const password = passwordInput.value;

            // 3. Cria o objeto JSON com os nomes que o BACK-END (Java) espera
            const loginData = {
                login: username, 
                senha: password 
            };

            // Limpa mensagens de erro antigas
            errorMessage.textContent = "";

            // 4. O Fetch aponta para a URL ABSOLUTA
            fetch("http://localhost:8080/api/auth/login", { 
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(loginData) 
            })
            .then(response => {
                if (response.ok) {
                    return response.json(); // Converte a resposta em JSON
                } else {
                    throw new Error('Falha no login'); // Causa o .catch()
                }
            })
            .then(data => {
                // Se chegou aqui, o login foi sucesso e 'data' é o JSON
                console.log("Login bem-sucedido:", data);
                
                // --- MUDANÇA APLICADA (Passo 2) ---
                
                // 1. Salva TODOS os dados do usuário no localStorage
                localStorage.setItem('userIsLoggedIn', 'true');
                localStorage.setItem('userName', data.nome);
                localStorage.setItem('userRole', data.cargo);
                localStorage.setItem('userSystem', data.sistema);
                localStorage.setItem('isSystemAdmin', data.isSystemAdmin);

                // 2. Aplica a regra de redirecionamento
                if (data.isSystemAdmin) {
                    // Se for Admin (ID=1), vai para a tela de seleção
                    window.location.href = "/pages/selecao-sistema.html"; 
                } else {
                    // Se for Profissional ou Supervisor, vai para a tela principal
                    window.location.href = "/pages/tela-principal.html"; 
                }
                // --- FIM DA MUDANÇA ---
            })
            .catch(error => {
                // Se response.ok foi false OU a rede falhou
                console.error("Erro na requisição:", error);
                
                if (error.message.includes('Failed to fetch')) {
                    errorMessage.textContent = "Não foi possível conectar ao servidor.";
                } else {
                    errorMessage.textContent = "Usuário ou senha inválidos.";
                }
            });
        });
    }
});