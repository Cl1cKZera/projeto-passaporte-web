package com.fateclins.passaporte.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "atividades")
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Integer ordem;

    // Define se é um molde (Template criado pelo RH) ou se é uma instância real
    @Column(nullable = false)
    private boolean isTemplate = false;

    // Relacionamento (Moldes pertencem a um Perfil)
    @ManyToOne 
    @JoinColumn(name = "perfil_id")
    private PerfilPassaporte perfil;

    // Relacionamento (Instâncias pertencem a um Passaporte)
    @ManyToOne
    @JoinColumn(name = "passaporte_id")
    private Passaporte passaporte;

    @Enumerated(EnumType.STRING)
    private StatusAcao status = StatusAcao.ABERTA;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private User responsavel;

    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;

    // Construtores
    public Atividade() {
    }

    public Atividade(String nome, String descricao, Integer ordem, PerfilPassaporte perfil, boolean isTemplate) {
        this.nome = nome;
        this.descricao = descricao;
        this.ordem = ordem;
        this.perfil = perfil;
        this.isTemplate = isTemplate;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isTemplate() { return isTemplate; }
    public void setTemplate(boolean template) { isTemplate = template; }

    public PerfilPassaporte getPerfil() { return perfil; }
    public void setPerfil(PerfilPassaporte perfil) { this.perfil = perfil; }

    public Passaporte getPassaporte() { return passaporte; }
    public void setPassaporte(Passaporte passaporte) { this.passaporte = passaporte; }

    public StatusAcao getStatus() { return status; }
    public void setStatus(StatusAcao status) { this.status = status; }

    public User getResponsavel() { return responsavel; }
    public void setResponsavel(User responsavel) { this.responsavel = responsavel; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }
}
