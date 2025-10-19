package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PESSOAFIS")
public class PessoaFis implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPESSOAFIS")
    private Integer idpessoafis;

    // Coluna do nome, baseado no seu banco de dados
    @Column(name = "NOMEPESSOA")
    private String nomepessoa;
    
    // Construtor vazio
    public PessoaFis() {}

    // Getters e Setters
    public Integer getIdpessoafis() {
        return idpessoafis;
    }
    public void setIdpessoafis(Integer idpessoafis) {
        this.idpessoafis = idpessoafis;
    }

    // Getter para o nome
    public String getNomepessoa() {
        return nomepessoa;
    }
    public void setNomepessoa(String nomepessoa) {
        this.nomepessoa = nomepessoa;
    }
}