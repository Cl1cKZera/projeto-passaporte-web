package com.fateclins.passaporte.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subtarefas")
public class Subtarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private boolean isTemplate = false;

    // A Subtarefa pertence a uma Tarefa
    @ManyToOne
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAcao status = StatusAcao.ABERTA;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private User responsavel;

    @OneToMany(mappedBy = "subtarefa", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"tarefa", "subtarefa", "dados"})
    private List<Artefato> artefatos;

    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;

    public Subtarefa() {}

    public Subtarefa(String nome, Integer ordem, Tarefa tarefa, boolean isTemplate) {
        this.nome = nome;
        this.ordem = ordem;
        this.tarefa = tarefa;
        this.isTemplate = isTemplate;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isTemplate() { return isTemplate; }
    public void setTemplate(boolean template) { isTemplate = template; }

    public Tarefa getTarefa() { return tarefa; }
    public void setTarefa(Tarefa tarefa) { this.tarefa = tarefa; }

    public StatusAcao getStatus() { return status; }
    public void setStatus(StatusAcao status) { this.status = status; }

    public User getResponsavel() { return responsavel; }
    public void setResponsavel(User responsavel) { this.responsavel = responsavel; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public List<Artefato> getArtefatos() { return artefatos; }
    public void setArtefatos(List<Artefato> artefatos) { this.artefatos = artefatos; }
}
