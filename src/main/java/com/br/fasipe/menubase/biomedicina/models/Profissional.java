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

    // --- MUDANÇA AQUI (Passo 1): Mapeando a coluna TIPOPROFI ---
    @Column(name = "TIPOPROFI")
    private String tipoProfi; // '1', '2', '3', ou '4'
    // --- FIM DA MUDANÇA ---

    @OneToOne
    @JoinColumn(name = "ID_PESSOAFIS")
    private PessoaFis pessoaFis;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "PROFI_ESPEC",
        joinColumns = @JoinColumn(name = "ID_PROFISSIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_ESPEC")
    )
    private Set<Especialidade> especialidades = new HashSet<>();

    // Getters e Setters

    // --- MUDANÇA AQUI: Getters e Setters para tipoProfi ---
    public String getTipoProfi() { return tipoProfi; }
    public void setTipoProfi(String tipoProfi) { this.tipoProfi = tipoProfi; }
    // --- FIM DA MUDANÇA ---

    public Integer getIdprofissio() {return idprofissio;}
    public void setIdprofissio(Integer idprofissio) { this.idprofissio = idprofissio; }
    
    public Integer getIdSupprofi() { return idSupprofi; }
    public void setIdSupprofi(Integer idSupprofi) { this.idSupprofi = idSupprofi; }
    
    public PessoaFis getPessoaFis() { return pessoaFis; }
    public void setPessoaFis(PessoaFis pessoaFis) { this.pessoaFis = pessoaFis; }

    public Set<Especialidade> getEspecialidades() { return especialidades; }
    public void setEspecialidades(Set<Especialidade> especialidades) { this.especialidades = especialidades; }
}