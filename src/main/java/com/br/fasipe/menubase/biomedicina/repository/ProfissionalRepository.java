package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

// Removemos a importação de java.util.Optional pois o método não é mais declarado aqui.

public interface ProfissionalRepository extends JpaRepository<Profissional, Integer> {

    // O método findById(Integer id) NÃO ESTÁ MAIS AQUI! Ele é herdado.

    // Remova qualquer outro método de busca por Setor que possa ter restado.
    // Opcional: Se quiser um método customizado, crie-o aqui.
}