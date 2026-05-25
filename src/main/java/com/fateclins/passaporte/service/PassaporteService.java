package com.fateclins.passaporte.service;

import com.fateclins.passaporte.model.*;
import com.fateclins.passaporte.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// @Service avisa ao Spring: "Esta classe é o coração da regra de negócio". 
// É aqui que processamos a inteligência antes de salvar no banco de dados.
@Service
public class PassaporteService {

    @Autowired
    private PassaporteRepository passaporteRepository;
    @Autowired
    private UserRepository userRepository;
    
    // Repositórios Unificados (Servem para Moldes e Instâncias)
    @Autowired
    private AtividadeRepository atividadeRepository;
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private SubtarefaRepository subtarefaRepository;

    public Passaporte create(Passaporte passaporte) {
        // 1. Validações do Candidato
        User candidato = userRepository.findById(passaporte.getCandidato().getId())
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

        if (candidato.getRole() != Role.CONTRATADO) {
            throw new RuntimeException("Atenção: Apenas usuários com perfil de CONTRATADO podem ter um passaporte!");
        }

        List<Passaporte> existentes = passaporteRepository.findByCandidatoId(candidato.getId());
        boolean jaPossui = existentes.stream()
                .anyMatch(p -> p.getPerfil().getId().equals(passaporte.getPerfil().getId()) 
                            && p.getStatus() != StatusAcao.CANCELADA);
        
        if (jaPossui) {
            throw new RuntimeException("Este candidato já possui um passaporte ativo para este perfil!");
        }

        // 2. Salva o Passaporte
        Passaporte savedPassaporte = passaporteRepository.save(passaporte);

        // ==========================================
        // A MÁGICA DA CLONAGEM (isTemplate)
        // Aqui nós pegamos as atividades que o RH criou como "Molde" (isTemplate = true) 
        // e geramos uma cópia exata delas para o candidato atual (isTemplate = false)
        // ==========================================
        
        // Passo A: Buscar os Moldes (isTemplate = true) de Atividade do Perfil
        List<Atividade> atividadesMolde = atividadeRepository.findByPerfilIdAndIsTemplateTrueOrderByOrdemAsc(savedPassaporte.getPerfil().getId());

        for (Atividade moldeAtiv : atividadesMolde) {
            // Passo B: Criar a Instância da Atividade (isTemplate = false)
            Atividade instanciaAtiv = new Atividade();
            instanciaAtiv.setNome(moldeAtiv.getNome());
            instanciaAtiv.setDescricao(moldeAtiv.getDescricao());
            instanciaAtiv.setOrdem(moldeAtiv.getOrdem());
            instanciaAtiv.setTemplate(false);
            instanciaAtiv.setPassaporte(savedPassaporte); // Aqui está o segredo
            instanciaAtiv.setStatus(StatusAcao.ABERTA);
            instanciaAtiv = atividadeRepository.save(instanciaAtiv);

            // Passo C: Buscar Tarefas do Molde
            List<Tarefa> tarefasMolde = tarefaRepository.findByAtividadeIdOrderByOrdemAsc(moldeAtiv.getId());
            
            for (Tarefa moldeTar : tarefasMolde) {
                // Passo D: Criar a Instância da Tarefa
                Tarefa instanciaTar = new Tarefa();
                instanciaTar.setNome(moldeTar.getNome());
                instanciaTar.setDescricao(moldeTar.getDescricao());
                instanciaTar.setOrdem(moldeTar.getOrdem());
                instanciaTar.setPrazoDias(moldeTar.getPrazoDias());
                instanciaTar.setTemplate(false);
                instanciaTar.setAtividade(instanciaAtiv); // Vincula à Atividade Instância
                instanciaTar.setStatus(StatusAcao.ABERTA);
                instanciaTar.setPrazoFinal(LocalDateTime.now().plusDays(moldeTar.getPrazoDias()));
                instanciaTar = tarefaRepository.save(instanciaTar);

                // Passo E: Buscar Subtarefas do Molde
                List<Subtarefa> subMolde = subtarefaRepository.findByTarefaIdOrderByOrdemAsc(moldeTar.getId());
                
                for (Subtarefa moldeSub : subMolde) {
                    // Passo F: Criar a Instância da Subtarefa
                    Subtarefa instanciaSub = new Subtarefa();
                    instanciaSub.setNome(moldeSub.getNome());
                    instanciaSub.setOrdem(moldeSub.getOrdem());
                    instanciaSub.setTemplate(false);
                    instanciaSub.setTarefa(instanciaTar); // Vincula à Tarefa Instância
                    instanciaSub.setStatus(StatusAcao.ABERTA);
                    subtarefaRepository.save(instanciaSub);
                }
            }
        }

        return savedPassaporte;
    }

    public List<Passaporte> findAll() {
        return passaporteRepository.findAll();
    }
    
    public List<Passaporte> findByCandidatoId(Long candidatoId) {
        return passaporteRepository.findByCandidatoId(candidatoId);
    }

    public Optional<Passaporte> findById(Long id) {
        return passaporteRepository.findById(id);
    }

    public void deleteById(Long id) {
        // Exclusão em cascata manual para evitar erros de Foreign Key Constraints
        Passaporte p = passaporteRepository.findById(id).orElse(null);
        if (p == null) return;
        
        List<Atividade> atividades = atividadeRepository.findByPassaporteIdOrderByOrdemAsc(id);
        for (Atividade a : atividades) {
            List<Tarefa> tarefas = tarefaRepository.findByAtividadeIdOrderByOrdemAsc(a.getId());
            for (Tarefa t : tarefas) {
                List<Subtarefa> subtarefas = subtarefaRepository.findByTarefaIdOrderByOrdemAsc(t.getId());
                for (Subtarefa sub : subtarefas) {
                    subtarefaRepository.delete(sub); // Deleta a subtarefa (e seus artefatos via Cascade)
                }
                tarefaRepository.delete(t); // Deleta a tarefa (e seus artefatos via Cascade)
            }
            atividadeRepository.delete(a); // Deleta a atividade
        }
        
        passaporteRepository.deleteById(id); // Finalmente deleta o passaporte
    }
}
