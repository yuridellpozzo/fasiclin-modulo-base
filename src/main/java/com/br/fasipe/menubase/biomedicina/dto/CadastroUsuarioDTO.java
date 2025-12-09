package com.br.fasipe.menubase.biomedicina.dto;

public class CadastroUsuarioDTO {
    // Pessoais
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    
    // Endereço e Origem (NOVOS)
    private String cep;
    private String bairro;
    private String numeroEndereco;
    private String complemento;
    private String cidadeReside;
    private String estadoReside;
    private String cidadeNascimento;
    private String estadoNascimento;

    // Login
    private String login;
    private String senha;
    private String tipoProfi;
    private Integer idEspec;

    // --- GETTERS E SETTERS ---
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getNumeroEndereco() { return numeroEndereco; }
    public void setNumeroEndereco(String numeroEndereco) { this.numeroEndereco = numeroEndereco; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getCidadeReside() { return cidadeReside; }
    public void setCidadeReside(String cidadeReside) { this.cidadeReside = cidadeReside; }
    public String getEstadoReside() { return estadoReside; }
    public void setEstadoReside(String estadoReside) { this.estadoReside = estadoReside; }
    public String getCidadeNascimento() { return cidadeNascimento; }
    public void setCidadeNascimento(String cidadeNascimento) { this.cidadeNascimento = cidadeNascimento; }
    public String getEstadoNascimento() { return estadoNascimento; }
    public void setEstadoNascimento(String estadoNascimento) { this.estadoNascimento = estadoNascimento; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTipoProfi() { return tipoProfi; }
    public void setTipoProfi(String tipoProfi) { this.tipoProfi = tipoProfi; }
    public Integer getIdEspec() { return idEspec; }
    public void setIdEspec(Integer idEspec) { this.idEspec = idEspec; }
}