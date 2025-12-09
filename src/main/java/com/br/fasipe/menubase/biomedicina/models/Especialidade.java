package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ESPECIALIDADE")
public class Especialidade {

    @Id
    @Column(name = "IDESPEC")
    private Integer id;

    @Column(name = "DESCESPEC")
    private String descespec;

    // Getters e Setters
    public Integer getIdespec() {
        return id;
    }

    public void setIdespec(Integer id) {
        this.id = id;
    }

    public String getDescespec() {
        return descespec;
    }

    public void setDescespec(String descespec) {
        this.descespec = descespec;
    }
}