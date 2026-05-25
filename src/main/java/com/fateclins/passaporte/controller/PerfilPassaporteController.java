package com.fateclins.passaporte.controller;

import com.fateclins.passaporte.model.PerfilPassaporte;
import com.fateclins.passaporte.service.PerfilPassaporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfis")
@CrossOrigin(origins = "*")
public class PerfilPassaporteController {

    @Autowired
    private PerfilPassaporteService perfilService;

    // GET /api/perfis
    @GetMapping
    public List<PerfilPassaporte> getAll() {
        return perfilService.findAll();
    }

    // GET /api/perfis/ativos
    // Endpoint específico para buscar apenas os perfis que não foram desativados
    @GetMapping("/ativos")
    public List<PerfilPassaporte> getAtivos() {
        return perfilService.findAtivos();
    }

    // POST /api/perfis
    @PostMapping
    public PerfilPassaporte create(@RequestBody PerfilPassaporte perfil) {
        return perfilService.save(perfil);
    }

    // GET /api/perfis/1
    @GetMapping("/{id}")
    public ResponseEntity<PerfilPassaporte> getById(@PathVariable Long id) {
        return perfilService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/perfis/1/desativar
    // Usamos PATCH (atualização parcial) pois não estamos deletando, só mudando o status.
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        try {
            perfilService.desativar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
