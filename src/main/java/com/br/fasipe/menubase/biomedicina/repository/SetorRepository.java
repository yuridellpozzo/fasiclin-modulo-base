package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SetorRepository extends JpaRepository<Setor, Integer> {

    // --- CORREÇÃO AQUI ---
    // O nome do método deve seguir o caminho: Objeto (Profissional) + Atributo (Id)
    // Antes era: findByProfissionalIdprofissio
    // Agora é: findByProfissionalId
    List<Setor> findByProfissionalId(Integer idProfissional);
}