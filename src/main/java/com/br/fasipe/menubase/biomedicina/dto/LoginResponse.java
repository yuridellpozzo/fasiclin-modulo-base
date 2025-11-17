package com.br.fasipe.menubase.biomedicina.dto;

import java.util.List;

public class LoginResponse {
    private String nome;
    private String cargo;
    private boolean isSystemAdmin; // Mantemos para compatibilidade, mas usaremos tipoProfi
    
    // --- MUDANÇA AQUI (Passo 1): Novos campos para a regra de negócio ---
    private String tipoProfi; 
    private List<EspecialidadeDTO> especialidades;
    // --- FIM DA MUDANÇA ---

    public LoginResponse(String nome, String cargo, boolean isSystemAdmin, String tipoProfi, List<EspecialidadeDTO> especialidades) {
        this.nome = nome;
        this.cargo = cargo;
        this.isSystemAdmin = isSystemAdmin;
        this.tipoProfi = tipoProfi;
        this.especialidades = especialidades;
    }

    // Getters
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public boolean getIsSystemAdmin() { return isSystemAdmin; }
    
    // --- MUDANÇA AQUI: Novos Getters ---
    public String getTipoProfi() { return tipoProfi; }
    public List<EspecialidadeDTO> getEspecialidades() { return especialidades; }
}