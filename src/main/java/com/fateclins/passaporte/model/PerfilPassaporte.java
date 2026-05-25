package com.fateclins.passaporte.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perfil_passaporte")
public class PerfilPassaporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Integer versao = 1;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Construtores
    public PerfilPassaporte() {
    }

    public PerfilPassaporte(String nome, String descricao, Integer versao, Boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.versao = versao;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}

// (Motorista 1, Porteiro, etc) e possui regras específicas, como a de não poder ser
// O Perfil de Passaporte: Que representa as categorias profissionais 
// alterado após um passaporte ser criado (gerando uma nova versão) e a possibilidade de ser desativado. 

// O texto diz que um Perfil de Passaporte é, por exemplo, "MOTORISTA 1". Além disso, as histórias de usuário dizem que:
// Ele não deve ser simplesmente alterado se já estiver em uso, mas sim ganhar uma nova versão (HSrh202).
// Ele pode ser desativado mantendo o histórico (HSrh207).
// Por conta dessas regras, criei a entidade com dois campos especiais: versao e ativo.