package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PRONTUARIO")
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPRONTU")
    private Integer id;

    @Column(name = "ID_PACIENTE")
    private Integer idPaciente;

    @Column(name = "ID_PROFISSIO")
    private Integer idProfissio; // O estagiário continua como autor

    @Column(name = "ID_ESPEC")
    private Integer idEspec;

    @Column(name = "ID_PROCED")
    private Integer idProced;

    @Column(name = "DATAPROCED")
    private LocalDate dataProced;

    @Column(name = "DESCRPRONTU", columnDefinition = "TEXT")
    private String descrProntu;

    @Column(name = "LINKPROCED")
    private String linkProcedimento;

    @Column(name = "AUTOPACVISU")
    private Boolean autoPacVisu;

    // Construtor de conveniência para converter Temporário -> Oficial
    public Prontuario() {}

    // Getters e Setters... (Gere no VS Code ou copie o padrão acima)
    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }
    public Integer getIdProfissio() { return idProfissio; }
    public void setIdProfissio(Integer idProfissio) { this.idProfissio = idProfissio; }
    public Integer getIdEspec() { return idEspec; }
    public void setIdEspec(Integer idEspec) { this.idEspec = idEspec; }
    public Integer getIdProced() { return idProced; }
    public void setIdProced(Integer idProced) { this.idProced = idProced; }
    public LocalDate getDataProced() { return dataProced; }
    public void setDataProced(LocalDate dataProced) { this.dataProced = dataProced; }
    public String getDescrProntu() { return descrProntu; }
    public void setDescrProntu(String descrProntu) { this.descrProntu = descrProntu; }
    public Boolean getAutoPacVisu() { return autoPacVisu; }
    public void setAutoPacVisu(Boolean autoPacVisu) { this.autoPacVisu = autoPacVisu; }
}