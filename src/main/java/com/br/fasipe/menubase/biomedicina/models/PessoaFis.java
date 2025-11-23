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

    // --- MUDANÇA CRÍTICA: REMOVIDO 'NOMEPESSOA' DAQUI ---
    // A coluna mudou para a tabela PESSOA, então tiramos daqui para não dar erro.
    // ----------------------------------------------------
    
    @Column(name = "ID_PESSOA") // Adicionamos o vínculo com a tabela PESSOA
    private Integer idPessoa;

    public PessoaFis() {}

    public Integer getIdpessoafis() { return idpessoafis; }
    public void setIdpessoafis(Integer idpessoafis) { this.idpessoafis = idpessoafis; }

    public Integer getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Integer idPessoa) { this.idPessoa = idPessoa; }
}