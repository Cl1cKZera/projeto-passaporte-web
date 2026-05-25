package com.fateclins.passaporte.controller;

import com.fateclins.passaporte.model.Passaporte;
import com.fateclins.passaporte.service.PassaporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController avisa ao Spring: "Esta classe é uma API REST. Os retornos serão em JSON, e não telas HTML."
@RestController
// @RequestMapping define o endereço (URL) base para chegar nesta classe. Ex: localhost:8080/api/passaportes
@RequestMapping("/api/passaportes")
// @CrossOrigin permite que o nosso frontend (que roda no navegador) consiga acessar esta API sem bloqueios de segurança (CORS)
@CrossOrigin(origins = "*")
public class PassaporteController {

    // @Autowired avisa ao Spring: "Injete (crie) o PassaporteService aqui para mim, não quero dar 'new' manualmente"
    @Autowired
    private PassaporteService passaporteService;

    // GET /api/passaportes - Lista todos
    // @GetMapping mapeia requisições do tipo GET (usado para BUSCAR e LER dados do banco).
    @GetMapping
    public ResponseEntity<List<Passaporte>> getAll() {
        return ResponseEntity.ok(passaporteService.findAll());
    }

    // POST /api/passaportes - Cria um novo (lembre que no JSON precisa passar o id do candidato e do perfil)
    // @PostMapping mapeia requisições do tipo POST (usado para CRIAR coisas novas no banco).
    @PostMapping
    // @RequestBody avisa: "Pegue o JSON que o Frontend mandou no corpo da requisição e transforme no objeto Passaporte do Java"
    public ResponseEntity<?> createPassaporte(@RequestBody Passaporte passaporte) {
        try {
            Passaporte saved = passaporteService.create(passaporte);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            // Se o candidato não for um "CONTRATADO", a exception do Service cai aqui
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/passaportes/1 - Busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<Passaporte> getById(@PathVariable Long id) {
        return passaporteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/passaportes/candidato/5 - Busca os passaportes de um candidato específico (HScd317)
    @GetMapping("/candidato/{candidatoId}")
    public List<Passaporte> getByCandidatoId(@PathVariable Long candidatoId) {
        return passaporteService.findByCandidatoId(candidatoId);
    }

    // @DeleteMapping mapeia requisições DELETE. O {id} na URL vira a variável @PathVariable no Java.
    // Exemplo: se o frontend chamar DELETE /api/passaportes/5, o Java sabe que o id é 5.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        passaporteService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
