package com.fateclins.passaporte.controller;

import com.fateclins.passaporte.model.User;
import com.fateclins.passaporte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Permite que o Frontend em outra porta (ex: Live Server) consiga chamar a API
public class UserController {

    @Autowired
    private UserService userService;

    // GET /api/users
    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    // POST /api/users
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            // O Service vai validar a regra de negócio. Se passar, salva.
            User savedUser = userService.save(user);
            return ResponseEntity.ok(savedUser); // Retorna 200 OK e o usuário salvo
        } catch (RuntimeException e) {
            // Se o Service lançar aquela Exception (login duplicado), cai aqui.
            return ResponseEntity.badRequest().body(e.getMessage()); // Retorna erro 400
        }
    }

    // GET /api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok) // Se achou, retorna 200 OK com o usuário
                .orElse(ResponseEntity.notFound().build()); // Se não achou, retorna 404 Not Found
    }

    // DELETE /api/users/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erro: Não é possível excluir este usuário pois ele possui passaportes vinculados a ele.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir usuário.");
        }
    }
}
