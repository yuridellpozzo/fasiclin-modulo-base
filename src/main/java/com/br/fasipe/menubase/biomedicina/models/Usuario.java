package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Integer id;

    @Column(name = "LOGUSUARIO", nullable = false, unique = true)
    private String login;

    @Column(name = "SENHAUSUA", nullable = false)
    private String senha;

    @OneToOne
    @JoinColumn(name = "ID_PROFISSIO")
    private Profissional profissional;

    // --- MAPEAMENTO DUPLO (GARANTIA) ---
    
    // 1. Vínculo direto com Pessoa Física (se existir)
    @OneToOne
    @JoinColumn(name = "ID_PESSOAFIS")
    private PessoaFis pessoaFis;

    // 2. Coluna de Documento (se existir)
    @Column(name = "ID_DOCUMENTO")
    private String idDocumento; 

    // -----------------------------------

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USUAPERMIUSUA",
        joinColumns = @JoinColumn(name = "ID_USUARIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_PERMIUSUA")
    )
    private Set<Permiusua> permissoes = new HashSet<>();

    public Usuario() {}

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }

    // Getters dos novos campos
    public PessoaFis getPessoaFis() { return pessoaFis; }
    public void setPessoaFis(PessoaFis pessoaFis) { this.pessoaFis = pessoaFis; }
    
    public String getIdDocumento() { return idDocumento; }
    public void setIdDocumento(String idDocumento) { this.idDocumento = idDocumento; }
    
    public Set<Permiusua> getPermissoes() { return permissoes; }
    public void setPermissoes(Set<Permiusua> permissoes) { this.permissoes = permissoes; }
}