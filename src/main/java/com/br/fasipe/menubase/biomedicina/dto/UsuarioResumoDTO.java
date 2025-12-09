package com.br.fasipe.menubase.biomedicina.dto;

public class UsuarioResumoDTO {
    private Integer idUsuario;
    private Integer idProfissional;
    private String nome;
    private String login;
    private String tipoProfi; // 1, 2, 3, 4
    private String cpf;

    public UsuarioResumoDTO(Integer idUsuario, Integer idProfissional, String nome, String login, String tipoProfi, String cpf) {
        this.idUsuario = idUsuario;
        this.idProfissional = idProfissional;
        this.nome = nome;
        this.login = login;
        this.tipoProfi = tipoProfi;
        this.cpf = cpf;
    }

    // Getters
    public Integer getIdUsuario() { return idUsuario; }
    public Integer getIdProfissional() { return idProfissional; }
    public String getNome() { return nome; }
    public String getLogin() { return login; }
    public String getTipoProfi() { return tipoProfi; }
    public String getCpf() { return cpf; }
}