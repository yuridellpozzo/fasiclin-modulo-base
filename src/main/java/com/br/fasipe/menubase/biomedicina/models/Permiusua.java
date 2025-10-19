package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PERMIUSUA")
public class Permiusua {

    @Id
    @Column(name = "IDPERMIUSUA")
    private Integer id;

    @Column(name = "DESCPERMI")
    private String descpermi;

    // Getters e Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDescpermi() {
        return descpermi;
    }
    public void setDescpermi(String descpermi) {
        this.descpermi = descpermi;
    }
}