package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SetorRepository extends JpaRepository<Setor, Integer> {
    // Agora o método retorna uma lista de setores, pois um profissional pode estar em vários.
    List<Setor> findByProfissionalIdprofissio(Integer id);

    Optional<Setor> findByNome(String nome);
    boolean existsByNome(String nome);
}