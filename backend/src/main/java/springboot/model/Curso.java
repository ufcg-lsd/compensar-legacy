package springboot.model;

import java.util.List;

public class Curso {
    private String video;

    private String descricao;

    private List<String> exemplos; //Tambem são videos

    public Curso(String video, String descricao, List<String> exemplos) {
        this.video = video;
        this.descricao = descricao;
        this.exemplos = exemplos;
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
}
