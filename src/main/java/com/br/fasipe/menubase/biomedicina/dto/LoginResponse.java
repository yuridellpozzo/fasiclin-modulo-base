package com.br.fasipe.menubase.biomedicina.dto;

public class LoginResponse {
    private String nome;
    private String cargo;
    private boolean isSystemAdmin;
    private String sistema;

    public LoginResponse(String nome, String cargo, boolean isSystemAdmin, String sistema) {
        this.nome = nome;
        this.cargo = cargo;
        this.isSystemAdmin = isSystemAdmin;
        this.sistema = sistema;
    }

    // Getters
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public boolean getIsSystemAdmin() { return isSystemAdmin; }
    public String getSistema() { return sistema; }
}