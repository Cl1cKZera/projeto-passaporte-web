package com.fateclins.passaporte.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Integer prazoDias;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private boolean isTemplate = false;

    // A Tarefa sempre pertence a uma Atividade (seja molde ou instância)
    @ManyToOne
    @JoinColumn(name = "atividade_id", nullable = false)
    private Atividade atividade;

    @Enumerated(EnumType.STRING)
    private StatusAcao status = StatusAcao.ABERTA;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private User responsavel;

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"tarefa", "subtarefa", "dados"}) // Ignora os bytes para o JSON não ficar gigantesco
    private List<Artefato> artefatos;

    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    private LocalDateTime prazoFinal;

    public Tarefa() {}

    public Tarefa(String nome, String descricao, Integer prazoDias, Integer ordem, Atividade atividade, boolean isTemplate) {
        this.nome = nome;
        this.descricao = descricao;
        this.prazoDias = prazoDias;
        this.ordem = ordem;
        this.atividade = atividade;
        this.isTemplate = isTemplate;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getPrazoDias() { return prazoDias; }
    public void setPrazoDias(Integer prazoDias) { this.prazoDias = prazoDias; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isTemplate() { return isTemplate; }
    public void setTemplate(boolean template) { isTemplate = template; }

    public Atividade getAtividade() { return atividade; }
    public void setAtividade(Atividade atividade) { this.atividade = atividade; }

    public List<Artefato> getArtefatos() {
        return artefatos;
    }

    public void setArtefatos(List<Artefato> artefatos) {
        this.artefatos = artefatos;
    }

    public StatusAcao getStatus() { return status; }
    public void setStatus(StatusAcao status) { this.status = status; }

    public User getResponsavel() { return responsavel; }
    public void setResponsavel(User responsavel) { this.responsavel = responsavel; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public LocalDateTime getPrazoFinal() { return prazoFinal; }
    public void setPrazoFinal(LocalDateTime prazoFinal) { this.prazoFinal = prazoFinal; }
}
