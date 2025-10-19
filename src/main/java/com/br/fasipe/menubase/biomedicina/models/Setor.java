package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;

@Entity
@Table(name = "SETOR")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDSETOR")
    private Integer id;

    // O relacionamento agora é mapeado a partir de Setor, que tem a chave estrangeira
    @ManyToOne
    @JoinColumn(name = "ID_PROFISSIO", nullable = false)
    private Profissional profissional;

    @Column(name = "NOMESETOR", nullable = false, unique = true, length = 50)
    private String nome;

    public Setor() {
    }

    public Setor(Integer id, Profissional profissional, String nome) {
        this.id = id;
        this.profissional = profissional;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setor setor = (Setor) o;
        return Objects.equals(id, setor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Setor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}