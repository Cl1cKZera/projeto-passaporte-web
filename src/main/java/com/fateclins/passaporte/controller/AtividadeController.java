package com.fateclins.passaporte.controller;

import com.fateclins.passaporte.model.Atividade;
import com.fateclins.passaporte.service.AtividadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atividades")
@CrossOrigin(origins = "*")
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;

    // POST /api/atividades - Cria uma nova atividade
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Atividade atividade) {
        try {
            Atividade saved = atividadeService.save(atividade);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/atividades/perfil/2 - Busca as atividades de um perfil, JÁ ORDENADAS (HSrh204)
    @GetMapping("/perfil/{perfilId}")
    public List<Atividade> getByPerfilId(@PathVariable Long perfilId) {
        return atividadeService.getAtividadesByPerfil(perfilId);
    }

    // GET /api/atividades/1
    @GetMapping("/{id}")
    public ResponseEntity<Atividade> getById(@PathVariable Long id) {
        return atividadeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/atividades/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        atividadeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
