package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROFISSIONAL")
public class Profissional implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPROFISSIO")
    private Integer id;

    // Coluna do supervisor, baseado no seu banco
    @Column(name = "ID_SUPPROFI")
    private Integer idSupprofi;

    // Relacionamento com PessoaFis (para buscar o nome)
    @OneToOne
    @JoinColumn(name = "ID_PESSOAFIS")
    private PessoaFis pessoaFis;

    // Construtor vazio
    public Profissional() {}

    // Getters e Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    // Getter para o ID do supervisor
    public Integer getIdSupprofi() {
        return idSupprofi;
    }
    public void setIdSupprofi(Integer idSupprofi) {
        this.idSupprofi = idSupprofi;
    }

    // Getter para o objeto PessoaFis
    public PessoaFis getPessoaFis() {
        return pessoaFis;
    }
    public void setPessoaFis(PessoaFis pessoaFis) {
        this.pessoaFis = pessoaFis;
    }
}