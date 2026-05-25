package com.fateclins.passaporte.repository;

import com.fateclins.passaporte.model.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    
    // Busca moldes de um perfil
    List<Atividade> findByPerfilIdAndIsTemplateTrueOrderByOrdemAsc(Long perfilId);
    
    // Busca instâncias de um passaporte
    List<Atividade> findByPassaporteIdOrderByOrdemAsc(Long passaporteId);
}
