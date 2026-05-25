package com.fateclins.passaporte.repository;

import com.fateclins.passaporte.model.Artefato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtefatoRepository extends JpaRepository<Artefato, Long> {
    
    // Busca artefato de uma tarefa
    List<Artefato> findByTarefaId(Long tarefaId);

    // Busca artefato de uma subtarefa
    List<Artefato> findBySubtarefaId(Long subtarefaId);
}
