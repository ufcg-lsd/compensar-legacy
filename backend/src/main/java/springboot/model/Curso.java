package springboot.model;

import java.util.List;

public class Curso {
    private String video;

    private String descricao;

    private List<String> videoExemplos;

    private List<String> textoExemplos;

    public Curso(String video, String descricao, List<String> videoExemplos, List<String> textoExemplos) {
        this.video = video;
        this.descricao = descricao;
        this.videoExemplos = videoExemplos;
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

    public List<String> getVideoExemplos() {
        return videoExemplos;
    }

    public void setVideoExemplos(List<String> videoExemplos) {
        this.videoExemplos = videoExemplos;
    }

    public List<String> getTextoExemplos() {
        return textoExemplos;
    }

    public void setTextoExemplos(List<String> textoExemplos) {
        this.textoExemplos = textoExemplos;
    }
}