package com.br.fasipe.menubase.biomedicina.dto;

import java.util.List;

public class LoginResponse {
    private String nome;
    private String cargo;
    private boolean isSystemAdmin;
    private String sistema; // Este é o campo que faltava para o Supervisor!
    private String tipoProfi; 
    private List<EspecialidadeDTO> especialidades;

    // Construtor atualizado
    public LoginResponse(String nome, String cargo, boolean isSystemAdmin, String sistema, String tipoProfi, List<EspecialidadeDTO> especialidades) {
        this.nome = nome;
        this.cargo = cargo;
        this.isSystemAdmin = isSystemAdmin;
        this.sistema = sistema;
        this.tipoProfi = tipoProfi;
        this.especialidades = especialidades;
    }

    // Getters
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public boolean getIsSystemAdmin() { return isSystemAdmin; }
    public String getSistema() { return sistema; }
    public String getTipoProfi() { return tipoProfi; }
    public List<EspecialidadeDTO> getEspecialidades() { return especialidades; }
}