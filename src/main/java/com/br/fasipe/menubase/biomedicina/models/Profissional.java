package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PROFISSIONAL")
public class Profissional implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPROFISSIO")
    private Integer idprofissio;

    @Column(name = "ID_SUPPROFI")
    private Integer idSupprofi;

    @Column(name = "TIPOPROFI")
    private String tipoProfi;

    // --- CORREÇÃO: IDDOCUMENTO (Conforme print do DBeaver) ---
    @Column(name = "ID_DOCUMENTO")
    private String idDocumento;
    
    // REMOVIDO: private PessoaFis pessoaFis; (Não existe ID_PESSOAFIS aqui)

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "PROFI_ESPEC",
        joinColumns = @JoinColumn(name = "ID_PROFISSIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_ESPEC")
    )
    private Set<Especialidade> especialidades = new HashSet<>();

    // Getters e Setters
    public String getTipoProfi() { return tipoProfi; }
    public void setTipoProfi(String tipoProfi) { this.tipoProfi = tipoProfi; }
    public Integer getIdprofissio() {return idprofissio;}
    public void setIdprofissio(Integer idprofissio) { this.idprofissio = idprofissio; }
    public Integer getIdSupprofi() { return idSupprofi; }
    public void setIdSupprofi(Integer idSupprofi) { this.idSupprofi = idSupprofi; }
    
    public String getIdDocumento() { return idDocumento; }
    public void setIdDocumento(String idDocumento) { this.idDocumento = idDocumento; }

    public Set<Especialidade> getEspecialidades() { return especialidades; }
    public void setEspecialidades(Set<Especialidade> especialidades) { this.especialidades = especialidades; }
}