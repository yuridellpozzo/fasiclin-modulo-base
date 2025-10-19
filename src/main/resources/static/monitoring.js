document.getElementById("checkButton").addEventListener("click", checkConnections);

function checkConnections() {
    // URL que o Spring Actuator cria para nós
    const metricsUrl = "/actuator/metrics/";

    // Limpa os valores antigos
    document.getElementById("active-connections").textContent = "Carregando...";
    document.getElementById("idle-connections").textContent = "Carregando...";
    document.getElementById("pending-connections").textContent = "Carregando...";
    document.getElementById("max-connections").textContent = "Carregando...";

    // 1. Busca Conexões Ativas
    fetch(metricsUrl + "hikari.connections.active")
        .then(res => res.json())
        .then(data => {
            document.getElementById("active-connections").textContent = data.measurements[0].value;
        })
        .catch(err => {
            document.getElementById("active-connections").textContent = "Erro";
            console.error(err);
        });

    // 2. Busca Conexões Ociosas
    fetch(metricsUrl + "hikari.connections.idle")
        .then(res => res.json())
        .then(data => {
            document.getElementById("idle-connections").textContent = data.measurements[0].value;
        })
        .catch(err => {
            document.getElementById("idle-connections").textContent = "Erro";
            console.error(err);
        });
    
    // 3. Busca Conexões Pendentes (esperando na fila)
    fetch(metricsUrl + "hikari.connections.pending")
        .then(res => res.json())
        .then(data => {
            document.getElementById("pending-connections").textContent = data.measurements[0].value;
        })
        .catch(err => {
            document.getElementById("pending-connections").textContent = "Erro";
            console.error(err);
        });

    // 4. Busca Máximo de Conexões
    fetch(metricsUrl + "hikari.connections.max")
        .then(res => res.json())
        .then(data => {
            document.getElementById("max-connections").textContent = data.measurements[0].value;
        })
        .catch(err => {
            document.getElementById("max-connections").textContent = "Erro";
            console.error(err);
        });
}

// Carrega os dados na primeira vez que a página abre
checkConnections();