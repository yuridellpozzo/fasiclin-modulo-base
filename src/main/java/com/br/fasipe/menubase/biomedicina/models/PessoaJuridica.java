package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.util.Objects;

@Entity
@Table(name = "PESSOAJUR")
public class PessoaJuridica {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPESSOAJUR")
    private Integer id;
    
    @Column(name = "ID_PESSOA", unique = true)
    private Integer idPessoa;

    @Column(name = "CNPJ", nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(name = "RAZSOCIAL", nullable = false, length = 100)
    private String razaoSocial;

    @Column(name = "NOMEFAN", nullable = false, length = 100)
    private String nomeFantasia;

    @Column(name = "CNAE", length = 7)
    private String cnae;

    public PessoaJuridica() {
    }

    public PessoaJuridica(Integer id, Integer idPessoa, String cnpj, String razaoSocial, String nomeFantasia, String cnae) {
        this.id = id;
        this.idPessoa = idPessoa;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnae = cnae;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PessoaJuridica that = (PessoaJuridica) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PessoaJuridica{" +
                "id=" + id +
                ", cnpj='" + cnpj + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                '}';
    }
}
