package com.fateclins.passaporte.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// @Entity avisa ao Hibernate: "Crie uma tabela no Banco de Dados baseada nesta classe Java!"
@Entity
// @Table permite escolher o nome exato da tabela no banco (senão ele usaria o nome da classe 'Passaporte')
@Table(name = "passaportes")
public class Passaporte {

    // @Id avisa: "Este atributo é a Chave Primária (Primary Key) da tabela"
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) diz: "O banco de dados vai gerar esse número sozinho (Auto-Increment), não preciso passar na mão"
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento 1: Um Passaporte pertence a UM Candidato (Usuário)
    // @ManyToOne avisa: "MUITOS passaportes podem pertencer a UM usuário".
    @ManyToOne 
    // @JoinColumn cria a coluna física de Chave Estrangeira (Foreign Key) lá no banco, que vai apontar para o ID do candidato
    @JoinColumn(name = "candidato_id", nullable = false)
    private User candidato;

    // Relacionamento 2: Um Passaporte se baseia em UM Perfil de Passaporte
    @ManyToOne 
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilPassaporte perfil;

    // Quando um passaporte nasce, ele sempre nasce ABERTO.
    // @Enumerated(EnumType.STRING) salva o texto ("ABERTA") no banco, ao invés do número da posição no Enum (0, 1, 2)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAcao status = StatusAcao.ABERTA;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // Construtores
    public Passaporte() {
    }

    public Passaporte(User candidato, PerfilPassaporte perfil) {
        this.candidato = candidato;
        this.perfil = perfil;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCandidato() { return candidato; }
    public void setCandidato(User candidato) { this.candidato = candidato; }

    public PerfilPassaporte getPerfil() { return perfil; }
    public void setPerfil(PerfilPassaporte perfil) { this.perfil = perfil; }

    public StatusAcao getStatus() { return status; }
    public void setStatus(StatusAcao status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
