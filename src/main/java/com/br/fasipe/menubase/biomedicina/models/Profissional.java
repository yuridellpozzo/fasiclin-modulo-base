package com.br.fasipe.menubase.biomedicina.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PROFISSIONAL")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPROFISSIO")
    private Integer id;

    @Column(name = "TIPOPROFI")
    private String tipoProfi;

    @Column(name = "ID_DOCUMENTO")
    private Long idDocumento;

    @Column(name = "ID_CONSEPROFI")
    private Integer idConselho;

    @OneToOne(mappedBy = "profissional")
    private Usuario usuario;

    // --- REMOVIDO: private PessoaFis pessoaFis; --- 
    // (Removemos porque a coluna ID_PESSOAFIS não existe nesta tabela)

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "PROFI_ESPEC",
        joinColumns = @JoinColumn(name = "ID_PROFISSIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_ESPEC")
    )
    private Set<Especialidade> especialidades;

    // --- GETTERS E SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipoProfi() { return tipoProfi; }
    public void setTipoProfi(String tipoProfi) { this.tipoProfi = tipoProfi; }

    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }

    public Integer getIdConselho() { return idConselho; }
    public void setIdConselho(Integer idConselho) { this.idConselho = idConselho; }

    public Set<Especialidade> getEspecialidades() { return especialidades; }
    public void setEspecialidades(Set<Especialidade> especialidades) { this.especialidades = especialidades; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}