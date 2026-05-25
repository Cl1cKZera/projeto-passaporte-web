package com.fateclins.passaporte.repository;

import com.fateclins.passaporte.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByAtividadeIdOrderByOrdemAsc(Long atividadeId);
}
