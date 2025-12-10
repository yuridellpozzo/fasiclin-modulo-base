package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ESPECIALIDADE")
public class Especialidade {

    @Id
    @Column(name = "IDESPEC")
    private Integer id;

    @Column(name = "DESCESPEC")
    private String descEspec;

    // --- GETTERS E SETTERS ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescEspec() {
        return descEspec;
    }

    public void setDescEspec(String descEspec) {
        this.descEspec = descEspec;
    }

    // --- CORREÇÃO: CRIAMOS O MÉTODO getNome() ---
    // Ele serve de "apelido" para o descEspec, para o AuthController funcionar
    public String getNome() {
        return descEspec;
    }
}