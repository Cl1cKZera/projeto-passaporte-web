package com.fateclins.passaporte.controller;

import com.fateclins.passaporte.model.Atividade;
import com.fateclins.passaporte.model.Subtarefa;
import com.fateclins.passaporte.model.Tarefa;
import com.fateclins.passaporte.model.StatusAcao;
import com.fateclins.passaporte.repository.AtividadeRepository;
import com.fateclins.passaporte.repository.SubtarefaRepository;
import com.fateclins.passaporte.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "*")
public class PassaporteTrackingController {

    @Autowired
    private AtividadeRepository atividadeRepo;

    @Autowired
    private TarefaRepository tarefaRepo;

    @Autowired
    private SubtarefaRepository subtarefaRepo;

    // 1. Busca todas as Atividades instanciadas de um Passaporte
    @GetMapping("/passaporte/{passaporteId}/atividades")
    public List<Atividade> getAtividades(@PathVariable Long passaporteId) {
        return atividadeRepo.findByPassaporteIdOrderByOrdemAsc(passaporteId);
    }

    // 2. Busca todas as Tarefas instanciadas de uma Atividade
    @GetMapping("/atividade/{atividadeId}/tarefas")
    public List<Tarefa> getTarefas(@PathVariable Long atividadeId) {
        return tarefaRepo.findByAtividadeIdOrderByOrdemAsc(atividadeId);
    }

    // 3. Busca todas as Subtarefas instanciadas de uma Tarefa
    @GetMapping("/tarefa/{tarefaId}/subtarefas")
    public List<Subtarefa> getSubtarefas(@PathVariable Long tarefaId) {
        return subtarefaRepo.findByTarefaIdOrderByOrdemAsc(tarefaId);
    }

    // @PutMapping mapeia a alteração do status da Tarefa. E se todas as tarefas irmãs também estiverem VÁLIDAS, ele vai validar a ATIVIDADE pai sozinha!
    @PutMapping("/tarefa/{id}/status/{status}")
    public ResponseEntity<Tarefa> atualizarStatusTarefa(@PathVariable Long id, @PathVariable StatusAcao status) {
        Tarefa tarefa = tarefaRepo.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        tarefa.setStatus(status);
        
        if (status == StatusAcao.VALIDA) {
            tarefa.setDataConclusao(java.time.LocalDateTime.now());
        }
        
        tarefa = tarefaRepo.save(tarefa);

        // Automação: Se a Tarefa ficou válida, checa se todas as tarefas da Atividade também estão.
        if (status == StatusAcao.VALIDA) {
            Atividade parentAtiv = tarefa.getAtividade();
            List<Tarefa> irmas = tarefaRepo.findByAtividadeIdOrderByOrdemAsc(parentAtiv.getId());
            boolean todasValidas = irmas.stream().allMatch(t -> t.getStatus() == StatusAcao.VALIDA);

            if (todasValidas && parentAtiv.getStatus() != StatusAcao.VALIDA) {
                parentAtiv.setStatus(StatusAcao.VALIDA);
                parentAtiv.setDataConclusao(java.time.LocalDateTime.now());
                atividadeRepo.save(parentAtiv);
            }
        }

        return ResponseEntity.ok(tarefa);
    }

    // @PutMapping mapeia uma requisição PUT (usada para atualizar dados que já existem)
    // O objetivo desta rota é validar a Subtarefa e disparar a Mágica da Cascata (Automação)
    @PutMapping("/subtarefa/{id}/status/{status}")
    public ResponseEntity<Subtarefa> atualizarStatusSubtarefa(@PathVariable Long id, @PathVariable StatusAcao status) {
        Subtarefa subtarefa = subtarefaRepo.findById(id).orElseThrow(() -> new RuntimeException("Subtarefa não encontrada"));
        subtarefa.setStatus(status);
        
        if (status == StatusAcao.VALIDA) {
            subtarefa.setDataConclusao(java.time.LocalDateTime.now());
        }
        
        subtarefa = subtarefaRepo.save(subtarefa);

        // Automação: Se a Subtarefa ficou válida, checa se as outras subtarefas da mesma Tarefa também estão.
        if (status == StatusAcao.VALIDA) {
            Tarefa parentTarefa = subtarefa.getTarefa();
            List<Subtarefa> irmas = subtarefaRepo.findByTarefaIdOrderByOrdemAsc(parentTarefa.getId());
            boolean todasValidas = irmas.stream().allMatch(s -> s.getStatus() == StatusAcao.VALIDA);

            if (todasValidas && parentTarefa.getStatus() != StatusAcao.VALIDA) {
                // Ao invés de atualizar direto no banco, chamamos o método de atualizar a tarefa para ele desencadear a validação da Atividade
                atualizarStatusTarefa(parentTarefa.getId(), StatusAcao.VALIDA);
            }
        }

        return ResponseEntity.ok(subtarefa);
    }
}
