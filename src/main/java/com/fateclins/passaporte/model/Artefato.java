package com.fateclins.passaporte.model;

import jakarta.persistence.*;

// @Entity avisa que esta classe vai virar uma tabela no banco de dados para salvar nossos arquivos
@Entity
@Table(name = "artefatos")
public class Artefato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String tipoConteudo; // Ex: application/pdf, image/jpeg

    // @Lob (Large Object) avisa ao Hibernate: "Prepare-se, aqui vem um dado pesado (binário)".
    // É isso que nos permite salvar o PDF/Imagem direto no banco sem dar pau de limite de tamanho!
    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] dados; // O arquivo propriamente dito

    // Um artefato pode estar vinculado a uma Tarefa (ou Subtarefa)
    // @ManyToOne indica que Muitos artefatos podem pertencer a Uma mesma Tarefa
    @ManyToOne
    @JoinColumn(name = "tarefa_id", nullable = true)
    private Tarefa tarefa;

    @ManyToOne
    @JoinColumn(name = "subtarefa_id", nullable = true)
    private Subtarefa subtarefa;

    public Artefato() {
    }

    public Artefato(String nomeArquivo, String tipoConteudo, byte[] dados) {
        this.nomeArquivo = nomeArquivo;
        this.tipoConteudo = tipoConteudo;
        this.dados = dados;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getTipoConteudo() { return tipoConteudo; }
    public void setTipoConteudo(String tipoConteudo) { this.tipoConteudo = tipoConteudo; }

    public byte[] getDados() { return dados; }
    public void setDados(byte[] dados) { this.dados = dados; }

    public Tarefa getTarefa() { return tarefa; }
    public void setTarefa(Tarefa tarefa) { this.tarefa = tarefa; }

    public Subtarefa getSubtarefa() { return subtarefa; }
    public void setSubtarefa(Subtarefa subtarefa) { this.subtarefa = subtarefa; }
}
