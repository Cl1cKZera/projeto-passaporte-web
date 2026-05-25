package com.fateclins.passaporte.repository;

import com.fateclins.passaporte.model.Subtarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtarefaRepository extends JpaRepository<Subtarefa, Long> {
    List<Subtarefa> findByTarefaIdOrderByOrdemAsc(Long tarefaId);
}
