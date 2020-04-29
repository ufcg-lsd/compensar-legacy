package springboot.dto.output;

import springboot.model.ModuloCurso;

import java.util.List;

public class ModuloCursoOutput {
    private String nome;

    private ModuloCurso.EstadoModulo estado;

    private String video;

    private String descricao;

    private List<String> exemplos;

    private List<String> questoes;

    public ModuloCursoOutput(String nome, ModuloCurso.EstadoModulo estado, String video, String descricao, List<String> exemplos, List<String> questoes) {
        this.nome = nome;
        this.estado = estado;
        this.video = video;
        this.descricao = descricao;
        this.exemplos = exemplos;
        this.questoes = questoes;
    }

    public ModuloCursoOutput() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ModuloCurso.EstadoModulo getEstado() {
        return estado;
    }

    public void setEstado(ModuloCurso.EstadoModulo estado) {
        this.estado = estado;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getExemplos() {
        return exemplos;
    }

    public void setExemplos(List<String> exemplos) {
        this.exemplos = exemplos;
    }

    public List<String> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<String> questoes) {
        this.questoes = questoes;
    }
}
