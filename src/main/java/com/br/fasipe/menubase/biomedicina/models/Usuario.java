package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.util.HashSet; // Importar
import java.util.Objects;
import java.util.Set; // Importar

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

    // --- ADICIONADO ---
    // Mapeia o link direto com PESSOAFIS que existe no seu banco
    @OneToOne
    @JoinColumn(name = "ID_PESSOAFIS")
    private PessoaFis pessoaFis;

    // --- ADICIONADO ---
    // Mapeia a tabela de permissões (USUAPERMIUSUA)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USUAPERMIUSUA",
        joinColumns = @JoinColumn(name = "ID_USUARIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_PERMIUSUA")
    )
    private Set<Permiusua> permissoes = new HashSet<>();
    
    // --- FIM DAS ADIÇÕES ---

    public Usuario() {
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Profissional getProfissional() { return profissional; }
    public void setProfissional(Profissional profissional) { this.profissional = profissional; }

    // --- NOVOS GETTERS E SETTERS ---
    public PessoaFis getPessoaFis() { return pessoaFis; }
    public void setPessoaFis(PessoaFis pessoaFis) { this.pessoaFis = pessoaFis; }
    public Set<Permiusua> getPermissoes() { return permissoes; }
    public void setPermissoes(Set<Permiusua> permissoes) { this.permissoes = permissoes; }
    // --- FIM DOS NOVOS GETTERS E SETTERS ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}