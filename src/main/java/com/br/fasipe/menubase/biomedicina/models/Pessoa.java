package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;

@Entity
@Table(name = "PESSOA")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPESSOA")
    private Integer idPessoa;

    @Column(name = "NOMEPESSOA")
    private String nome;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TIPOPESSOA")
    private String tipoPessoa; // 'F' ou 'J'

    @Column(name = "ID_DOCUMENTO")
    private Long idDocumento;

    @Column(name = "TELEFONE")
    private String telefone;

    @Column(name = "CEP")
    private String cep;

    @Column(name = "BAIRRO")
    private String bairro;

    @Column(name = "NUMERO_ENDERECO")
    private String numeroEndereco;

    @Column(name = "COMPLEMENTO")
    private String complemento;

    @Column(name = "ESTADO_RESIDE")
    private String estadoReside;

    @Column(name = "CIDADE_RESIDE")
    private String cidadeReside;

    @Column(name = "ESTADO_NASCIMENTO")
    private String estadoNascimento;

    @Column(name = "CIDADE_NASCIMENTO")
    private String cidadeNascimento;

    // Construtor Padrão
    public Pessoa() {
        // Define padrão 'F' (Física) se não for informado
        this.tipoPessoa = "F"; 
    }

    // --- GETTERS E SETTERS ---

    public Integer getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Integer idPessoa) { this.idPessoa = idPessoa; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoPessoa() { return tipoPessoa; }
    public void setTipoPessoa(String tipoPessoa) { this.tipoPessoa = tipoPessoa; }

    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    //endereço
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getNumeroEndereco() { return numeroEndereco; }
    public void setNumeroEndereco(String numeroEndereco) { this.numeroEndereco = numeroEndereco; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getEstadoReside() { return estadoReside; }
    public void setEstadoReside(String estadoReside) { this.estadoReside = estadoReside; }
    public String getCidadeReside() { return cidadeReside; }
    public void setCidadeReside(String cidadeReside) { this.cidadeReside = cidadeReside; }
    public String getEstadoNascimento() { return estadoNascimento; }
    public void setEstadoNascimento(String estadoNascimento) { this.estadoNascimento = estadoNascimento; }
    public String getCidadeNascimento() { return cidadeNascimento; }
    public void setCidadeNascimento(String cidadeNascimento) { this.cidadeNascimento = cidadeNascimento; }
}