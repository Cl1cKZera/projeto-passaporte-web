package com.fateclins.passaporte.repository;

import com.fateclins.passaporte.model.Passaporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassaporteRepository extends JpaRepository<Passaporte, Long> {
    
    // Método mágico: "Buscar todos os passaportes filtrando pelo ID do candidato"
    // Útil para a tela onde o candidato loga e vê apenas os passaportes dele (HScd317)
    List<Passaporte> findByCandidatoId(Long candidatoId);
}
