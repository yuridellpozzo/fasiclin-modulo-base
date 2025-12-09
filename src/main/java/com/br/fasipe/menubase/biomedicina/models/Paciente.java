package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;

@Entity
@Table(name = "PACIENTE")
public class Paciente {
    
    @Id
    @Column(name = "IDPACIENTE")
    private Integer id;

    // Usaremos o RG para identificar o paciente na lista
    @Column(name = "RGPACIENTE")
    private String rg;

    public Paciente() {}

    // Getters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
}