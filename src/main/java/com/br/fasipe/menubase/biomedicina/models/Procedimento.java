package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PROCEDIMENTO")
public class Procedimento {
    
    @Id
    @Column(name = "IDPROCED")
    private Integer id;

    @Column(name = "DESCRPROC") // Nome exato conforme seu print
    private String descricao;

    @Column(name = "CODPROCED")
    private String codigo;
    
    @Column(name = "VALORPROC")
    private BigDecimal valor;

    public Procedimento() {}

    // Getters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}