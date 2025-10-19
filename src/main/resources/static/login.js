document.addEventListener("DOMContentLoaded", () => {
    
    // 1. Pega os elementos pelos IDs corretos do SEU HTML
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

            // 4. O Fetch agora aponta para a URL ABSOLUTA
            fetch("http://localhost:8080/api/auth/login", { 
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(loginData) 
            })
            // --- MUDANÇA: Processar a resposta JSON ---
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
                
                // Salva os dados do usuário no localStorage
                localStorage.setItem('userIsLoggedIn', 'true');
                localStorage.setItem('userName', data.nome);
                localStorage.setItem('userRole', data.cargo);

                // Redireciona para a tela principal
                window.location.href = "/pages/tela-principal.html"; 

            })
            .catch(error => {
                // ESTE É O ÚNICO BLOCO .catch()
                // Se response.ok foi false OU a rede falhou
                console.error("Erro na requisição:", error);
                
                // Mostra a mensagem de erro correta
                if (error.message.includes('Failed to fetch')) {
                    errorMessage.textContent = "Não foi possível conectar ao servidor.";
                } else {
                    errorMessage.textContent = "Usuário ou senha inválidos.";
                }
            });
            // O .catch() duplicado foi removido daqui
        });
    }
    // A CHAVE '}' EXTRA FOI REMOVIDA DAQUI
});