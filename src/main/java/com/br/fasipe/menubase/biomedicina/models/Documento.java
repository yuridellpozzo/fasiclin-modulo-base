package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;

@Entity
@Table(name = "DOCUMENTO")
public class Documento {

    @Id
    @Column(name = "DOCUMENTO") // Conforme seu print
    private Long id;

    // Construtores
    public Documento() {}
    public Documento(Long id) { this.id = id; }

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}