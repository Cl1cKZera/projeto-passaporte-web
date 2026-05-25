package com.fateclins.passaporte.repository;

import com.fateclins.passaporte.model.PerfilPassaporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilPassaporteRepository extends JpaRepository<PerfilPassaporte, Long> {
    
    // Método mágico do Spring: Busca todos os perfis onde "ativo" seja igual a true
    List<PerfilPassaporte> findByAtivoTrue();
}
