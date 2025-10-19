package com.br.fasipe.menubase.biomedicina.dto;

// Um DTO simples para enviar os dados do usuário
public class LoginResponse {
    private String nome;
    private String cargo;

    public LoginResponse(String nome, String cargo) {
        this.nome = nome;
        this.cargo = cargo;
    }

    // Getters
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
}