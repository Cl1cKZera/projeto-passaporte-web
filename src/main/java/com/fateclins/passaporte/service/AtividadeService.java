package com.fateclins.passaporte.service;

import com.fateclins.passaporte.model.Atividade;
import com.fateclins.passaporte.repository.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtividadeService {

    @Autowired
    private AtividadeRepository atividadeRepository;

    public Atividade save(Atividade atividade) {
        // Regra de Negócio Básica: Garantir que a ordem não seja inválida
        if (atividade.getOrdem() == null || atividade.getOrdem() <= 0) {
            throw new RuntimeException("A ordem da atividade deve ser maior que zero (1, 2, 3...).");
        }
        
        return atividadeRepository.save(atividade);
    }

    public List<Atividade> getAtividadesByPerfil(Long perfilId) {
        return atividadeRepository.findByPerfilIdAndIsTemplateTrueOrderByOrdemAsc(perfilId);
    }

    public Optional<Atividade> findById(Long id) {
        return atividadeRepository.findById(id);
    }

    public void deleteById(Long id) {
        atividadeRepository.deleteById(id);
    }
}
