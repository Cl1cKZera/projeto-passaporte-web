package com.fateclins.passaporte.service;

import com.fateclins.passaporte.model.User;
import com.fateclins.passaporte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        // Lógica de negócio: Verificar se o username já existe antes de salvar
        Optional<User> userExistente = userRepository.findByUsername(user.getUsername());
        
        // Se estamos criando um usuário novo e o login já existe, não podemos permitir.
        // (Se user.getId() for null, é uma criação. Se não for, é uma atualização).
        if (user.getId() == null && userExistente.isPresent()) {
            throw new RuntimeException("Já existe um usuário cadastrado com este login!");
        }

        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
