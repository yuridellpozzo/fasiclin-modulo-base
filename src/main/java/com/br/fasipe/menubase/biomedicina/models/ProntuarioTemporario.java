package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRONTUARIO_TEMPORARIO")
public class ProntuarioTemporario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPRONTU_TEMP") // Confirme se no banco é esse nome ou 'IDPRONTU'
    private Integer id;

    @Column(name = "ID_PACIENTE")
    private Integer idPaciente; // Mantendo como Integer conforme seu padrão atual

    // --- RELACIONAMENTO CORRIGIDO ---
    @ManyToOne 
    @JoinColumn(name = "ID_PROFISSIO")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario", "especialidades"})
    private Profissional aluno;

    @Column(name = "ID_ESPEC") private Integer idEspec;
    @Column(name = "ID_PROCED") private Integer idProced;
    @Column(name = "DATAPROCED") private LocalDate dataProced;
    
    @Column(name = "DESCRPRONTU", columnDefinition = "TEXT")
    private String texto;

    @Column(name = "LINKPROCED")
    private String linkProced;

    @Column(name = "AUTOPACVISU")
    private Boolean autoPacVisu;

    @Column(name = "STATUS_APROVACAO")
    private String status;

    @Column(name = "ID_SUPERVISOR")
    private Integer idSupervisor;

    @Column(name = "DATA_DECISAO")
    private LocalDateTime dataDecisao;

    @Column(name = "MOTIVO_REPROVACAO", columnDefinition = "TEXT")
    private String observacaoSupervisor;

    // --- GETTERS E SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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

    public String getLinkProced() { return linkProced; }
    public void setLinkProced(String linkProced) { this.linkProced = linkProced; }

    public Boolean getAutoPacVisu() { return autoPacVisu; }
    public void setAutoPacVisu(Boolean autoPacVisu) { this.autoPacVisu = autoPacVisu; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(Integer idSupervisor) { this.idSupervisor = idSupervisor; }

    public LocalDateTime getDataDecisao() { return dataDecisao; }
    public void setDataDecisao(LocalDateTime dataDecisao) { this.dataDecisao = dataDecisao; }

    public String getObservacaoSupervisor() { return observacaoSupervisor; }
    public void setObservacaoSupervisor(String observacaoSupervisor) { this.observacaoSupervisor = observacaoSupervisor; }
}