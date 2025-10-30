package com.br.fasipe.menubase.biomedicina.dto;

public class MenuItemDTO {
    private String nome;
    private String url; // Para onde o botão vai linkar (ex: "/cad-paciente")

    public MenuItemDTO(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }

    // Getters
    public String getNome() { return nome; }
    public String getUrl() { return url; }
}