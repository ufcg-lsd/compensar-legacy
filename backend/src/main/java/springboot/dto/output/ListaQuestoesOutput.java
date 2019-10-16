package springboot.dto.output;

import springboot.model.ListaQuestoes;
import springboot.model.Questao;
import springboot.model.Usuario;

import java.util.List;

public class ListaQuestoesOutput {
    private String id;

    private String nomeLista;

    private String autor;

    private List<Questao> questoes;

    public ListaQuestoesOutput(String id, String nomeLista, String autor, List<Questao> questoes) {
        this.id = id;
        this.nomeLista = nomeLista;
        this.autor = autor;
        this.questoes = questoes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

}
