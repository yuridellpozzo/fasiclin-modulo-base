package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Integer id; // <--- Nome 'id'

    @Column(name = "LOGUSUARIO")
    private String login;

    @Column(name = "SENHAUSUA")
    private String senha;

    @Column(name = "ID_DOCUMENTO")
    private Long idDocumento;

    // Campos virtuais (Token de Senha - Memória)
    @Transient 
    private String tokenReset;
    @Transient
    private LocalDateTime dataExpiracaoToken;

    @OneToOne
    @JoinColumn(name = "ID_PESSOAFIS")
    private PessoaFis pessoaFis;

    @OneToOne
    @JoinColumn(name = "ID_PROFISSIO")
    private Profissional profissional;

    // --- GETTERS E SETTERS ---
    public Integer getId() { return id; } // getId()
    public void setId(Integer id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }

    public PessoaFis getPessoaFis() { return pessoaFis; }
    public void setPessoaFis(PessoaFis pessoaFis) { this.pessoaFis = pessoaFis; }

    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }

    public String getTokenReset() { return tokenReset; }
    public void setTokenReset(String tokenReset) { this.tokenReset = tokenReset; }
    public LocalDateTime getDataExpiracaoToken() { return dataExpiracaoToken; }
    public void setDataExpiracaoToken(LocalDateTime dataExpiracaoToken) { this.dataExpiracaoToken = dataExpiracaoToken; }
}