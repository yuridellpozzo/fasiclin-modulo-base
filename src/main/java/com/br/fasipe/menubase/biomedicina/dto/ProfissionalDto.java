package com.br.fasipe.menubase.biomedicina.dto;

import java.util.Objects;

public class ProfissionalDto {
    
    // ID do Profissional (para ser usado no backend)
    private Integer id; 
    
    // Nome da Pessoa Física (para ser exibido no dropdown do frontend)
    private String nomePessoa; 

    public ProfissionalDto() {
    }

    public ProfissionalDto(Integer id, String nomePessoa) {
        this.id = id;
        this.nomePessoa = nomePessoa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfissionalDto that = (ProfissionalDto) o;
        return Objects.equals(id, that.id) && Objects.equals(nomePessoa, that.nomePessoa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomePessoa);
    }
}