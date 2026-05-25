package com.fateclins.passaporte.service;

import com.fateclins.passaporte.model.PerfilPassaporte;
import com.fateclins.passaporte.repository.PerfilPassaporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilPassaporteService {

    @Autowired
    private PerfilPassaporteRepository perfilRepository;

    public PerfilPassaporte save(PerfilPassaporte perfil) {
        // Regra de Negócio: Ao atualizar (tem ID), devemos gerar uma nova versão?
        // O guia diz que não pode ser alterado se tiver passaporte, mas para um CRUD básico,
        // vamos apenas salvar por enquanto. A regra de versão será implementada depois.
        return perfilRepository.save(perfil);
    }

    public List<PerfilPassaporte> findAll() {
        return perfilRepository.findAll();
    }
    
    public List<PerfilPassaporte> findAtivos() {
        return perfilRepository.findByAtivoTrue();
    }

    public Optional<PerfilPassaporte> findById(Long id) {
        return perfilRepository.findById(id);
    }

    // Ao invés de deletar, a regra de negócio HSrh207 diz: "Um perfil pode ser desativado"
    public void desativar(Long id) {
        Optional<PerfilPassaporte> perfilOptional = perfilRepository.findById(id);
        
        if (perfilOptional.isPresent()) {
            PerfilPassaporte perfil = perfilOptional.get();
            perfil.setAtivo(false); // Apenas mudamos o status
            perfilRepository.save(perfil); // Atualiza no banco
        } else {
            throw new RuntimeException("Perfil não encontrado para desativar!");
        }
    }
}
