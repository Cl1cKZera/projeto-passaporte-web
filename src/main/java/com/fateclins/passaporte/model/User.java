package com.fateclins.passaporte.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Construtores
    public User() {
    }

    public User(String username, String password, String name, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

// O Usuário: Cada pessoa que acessa o sistema (RH, Gerente, etc.). 
// No seu código, o 'User' é o modelo que representa essas pessoas, 
// armazenando login, senha, nome e o perfil (Role) de cada um.

// Nota de mentoria: Ao usar Enum para 'Role', você garante que ninguém crie
// acidentalmente um usuário "PORTEIRRO" (com erro de digitação). Só existem as
// opções definidas. Isso se chama Segurança em Tempo de Compilação!
