package com.br.fasipe.menubase.biomedicina.dto;

public class ProntuarioDTO {
    
    private Integer id; // <--- O CAMPO QUE FALTAVA PARA IDENTIFICAR EDIÇÃO
    private Integer idPaciente;
    private Integer idProced;
    private Integer idProfissional; 
    private Integer idEspec;
    private String texto;
    private String linkProcedimento;
    private Boolean autoPacVisu;

    // --- GETTERS E SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public Integer getIdProced() { return idProced; }
    public void setIdProced(Integer idProced) { this.idProced = idProced; }

    public Integer getIdProfissional() { return idProfissional; }
    public void setIdProfissional(Integer idProfissional) { this.idProfissional = idProfissional; }

    public Integer getIdEspec() { return idEspec; }
    public void setIdEspec(Integer idEspec) { this.idEspec = idEspec; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getLinkProcedimento() { return linkProcedimento; }
    public void setLinkProcedimento(String linkProcedimento) { this.linkProcedimento = linkProcedimento; }

    public Boolean getAutoPacVisu() { return autoPacVisu; }
    public void setAutoPacVisu(Boolean autoPacVisu) { this.autoPacVisu = autoPacVisu; }
}