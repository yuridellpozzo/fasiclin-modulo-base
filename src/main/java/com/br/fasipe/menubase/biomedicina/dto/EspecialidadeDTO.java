package com.br.fasipe.menubase.biomedicina.dto;

// --- ARQUIVO NOVO (Passo 1): Para transportar dados das especialidades ---
public class EspecialidadeDTO {
    private Integer id;
    private String nome;

    public EspecialidadeDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }
}