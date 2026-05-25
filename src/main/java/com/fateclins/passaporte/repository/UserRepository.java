package com.fateclins.passaporte.repository;

import com.fateclins.passaporte.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // O Spring Data JPA já fornece save(), findAll(), findById(), deleteById()
    
    // Método customizado para buscar o usuário pelo login (username)
    Optional<User> findByUsername(String username);
}
