package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PRONTUARIO_TEMPORARIO")
public class ProntuarioTemporario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPRONTU_TEMP")
    private Integer idProntuTemp;

    @Column(name = "ID_PACIENTE")
    private Integer idPaciente;

    @OneToOne
    @JoinColumn(name = "ID_PROFISSIO")
    private Profissional aluno;

    @Column(name = "ID_ESPEC")
    private Integer idEspec;

    @Column(name = "ID_PROCED")
    private Integer idProced;

    @Column(name = "DATAPROCED")
    private LocalDate dataProced;

    @Column(name = "DESCRPRONTU", columnDefinition = "TEXT")
    private String texto;

    @Column(name = "LINKPROCED")
    private String linkProcedimento;

    // --- CORREÇÃO AQUI: DE 'tinyint(1)' PARA 'Boolean' ---
    @Column(name = "AUTOPACVISU")
    private Boolean autoPacVisu;

    // Enum no Banco: 'PENDENTE','APROVADO','REPROVADO'
    @Column(name = "STATUS_APROVACAO")
    private String status; 

    @Column(name = "ID_SUPERVISOR")
    private Integer idSupervisor;
    
    @Column(name = "DATA_DECISAO")
    private java.time.LocalDateTime dataDecisao; // Usei LocalDateTime para data e hora exata

    @Column(name = "MOTIVO_REPROVACAO", columnDefinition = "TEXT")
    private String observacaoSupervisor;

    public ProntuarioTemporario() {}

    // --- GETTERS E SETTERS ---

    public Integer getId() { return idProntuTemp; }
    public void setId(Integer idProntuTemp) { this.idProntuTemp = idProntuTemp; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public Profissional getAluno() { return aluno; }
    public void setAluno(Profissional aluno) { this.aluno = aluno; }

    public Integer getIdEspec() { return idEspec; }
    public void setIdEspec(Integer idEspec) { this.idEspec = idEspec; }

    public Integer getIdProced() { return idProced; }
    public void setIdProced(Integer idProced) { this.idProced = idProced; }

    public LocalDate getDataProced() { return dataProced; }
    public void setDataProced(LocalDate dataProced) { this.dataProced = dataProced; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getLinkProcedimento() { return linkProcedimento; }
    public void setLinkProcedimento(String linkProcedimento) { this.linkProcedimento = linkProcedimento; }

    public Boolean getAutoPacVisu() { return autoPacVisu; }
    public void setAutoPacVisu(Boolean autoPacVisu) { this.autoPacVisu = autoPacVisu; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacaoSupervisor() { return observacaoSupervisor; }
    public void setObservacaoSupervisor(String observacaoSupervisor) { this.observacaoSupervisor = observacaoSupervisor; }

    // --- ADICIONADOS QUE FALTAVAM ---
    public Integer getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(Integer idSupervisor) { this.idSupervisor = idSupervisor; }

    public java.time.LocalDateTime getDataDecisao() { return dataDecisao; }
    public void setDataDecisao(java.time.LocalDateTime dataDecisao) { this.dataDecisao = dataDecisao; }
}