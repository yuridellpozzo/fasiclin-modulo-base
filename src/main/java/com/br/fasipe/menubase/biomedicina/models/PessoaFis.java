package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;

@Entity
@Table(name = "PESSOAFIS")
public class PessoaFis {

    @Id
    @Column(name = "IDPESSOAFIS")
    private Integer id; // Manual

    @Column(name = "ID_PESSOA")
    private Integer idPessoa;

    // --- CORREÇÃO AQUI: ADICIONANDO O CAMPO OBRIGATÓRIO ---
    @Column(name = "ID_DOCUMENTO")
    private Long idDocumento;

    public PessoaFis() {}

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Integer idPessoa) { this.idPessoa = idPessoa; }

    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }
}