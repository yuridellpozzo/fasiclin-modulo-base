package com.br.fasipe.menubase.biomedicina.dto;

public class EspecialidadeDTO {
    
    private Integer id;
    private String nome;

    // Construtor
    public EspecialidadeDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // --- GETTERS OBRIGATÓRIOS (O ERRO ESTAVA AQUI) ---
    
    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    
    // Setters (Opcionais, mas bom ter)
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}