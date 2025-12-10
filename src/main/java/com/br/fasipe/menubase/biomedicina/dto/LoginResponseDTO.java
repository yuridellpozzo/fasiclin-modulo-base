package com.br.fasipe.menubase.biomedicina.dto;

import java.util.List;

public class LoginResponseDTO {
    private String token;
    private String nome;
    private String cargo;
    private String tipoProfi;
    private String sistema;
    private boolean isSystemAdmin;
    private List<String> especialidades; // LISTA DE STRINGS SIMPLES
    private Long idProfissional;

    public LoginResponseDTO(String token, String nome, String cargo, String tipoProfi, String sistema, boolean isSystemAdmin, List<String> especialidades, Long idProfissional) {
        this.token = token;
        this.nome = nome;
        this.cargo = cargo;
        this.tipoProfi = tipoProfi;
        this.sistema = sistema;
        this.isSystemAdmin = isSystemAdmin;
        this.especialidades = especialidades;
        this.idProfissional = idProfissional;
    }

    // Getters
    public String getToken() { return token; }
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public String getTipoProfi() { return tipoProfi; }
    public String getSistema() { return sistema; }
    public boolean getIsSystemAdmin() { return isSystemAdmin; }
    public List<String> getEspecialidades() { return especialidades; }
    public Long getIdProfissional() { return idProfissional; }
}