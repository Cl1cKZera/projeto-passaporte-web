package com.fateclins.passaporte.controller;

import com.fateclins.passaporte.model.Artefato;
import com.fateclins.passaporte.model.Subtarefa;
import com.fateclins.passaporte.model.Tarefa;
import com.fateclins.passaporte.repository.ArtefatoRepository;
import com.fateclins.passaporte.repository.SubtarefaRepository;
import com.fateclins.passaporte.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/artefatos")
@CrossOrigin(origins = "*")
public class ArtefatoController {

    @Autowired
    private ArtefatoRepository artefatoRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private SubtarefaRepository subtarefaRepository;

    // @PostMapping mapped para salvar arquivo na Tarefa.
    // O @RequestParam("file") MultipartFile é a mágica do Spring que pega o arquivo físico (PDF/JPG) enviado pelo Frontend e converte pra Java
    @PostMapping("/tarefa/{tarefaId}")
    public ResponseEntity<String> uploadParaTarefa(@PathVariable Long tarefaId, @RequestParam("file") MultipartFile file) {
        try {
            Tarefa tarefa = tarefaRepository.findById(tarefaId).orElseThrow();
            Artefato artefato = new Artefato(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            artefato.setTarefa(tarefa);
            artefatoRepository.save(artefato);
            return ResponseEntity.ok("Arquivo salvo com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar arquivo.");
        }
    }

    // Upload de arquivo para uma Subtarefa
    @PostMapping("/subtarefa/{subtarefaId}")
    public ResponseEntity<String> uploadParaSubtarefa(@PathVariable Long subtarefaId, @RequestParam("file") MultipartFile file) {
        try {
            Subtarefa subtarefa = subtarefaRepository.findById(subtarefaId).orElseThrow();
            Artefato artefato = new Artefato(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            artefato.setSubtarefa(subtarefa);
            artefatoRepository.save(artefato);
            return ResponseEntity.ok("Arquivo salvo com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar arquivo.");
        }
    }

    // Endpoint de Download! O retorno é um ResponseEntity<byte[]> porque estamos devolvendo os "bits" do arquivo original
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadArtefato(@PathVariable Long id) {
        Artefato artefato = artefatoRepository.findById(id).orElseThrow();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + artefato.getNomeArquivo() + "\"")
                .contentType(MediaType.parseMediaType(artefato.getTipoConteudo()))
                .body(artefato.getDados());
    }

    // Exclusão do arquivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtefato(@PathVariable Long id) {
        if (!artefatoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        artefatoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
