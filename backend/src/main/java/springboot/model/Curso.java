package springboot.model;

import java.util.List;

public class Curso {
    private String video;

    private String descricao;

    private List<String> exemplos; //Tambem são videos

    private List<String> textoExemplos; //No mesmo formato da descrição

    public Curso(String video, String descricao, List<String> exemplos, List<String> textoExemplos) {
        this.video = video;
        this.descricao = descricao;
        this.exemplos = exemplos;
        this.textoExemplos = textoExemplos;
    }

    public Curso() {
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

    public List<String> getTextoExemplos() {
        return textoExemplos;
    }

    public void setTextoExemplos(List<String> textoExemplos) {
        this.textoExemplos = textoExemplos;
    }
}
