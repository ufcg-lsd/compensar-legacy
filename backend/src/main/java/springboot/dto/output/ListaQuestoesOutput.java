package springboot.dto.output;

import springboot.model.ListaQuestoes;
import springboot.model.Questao;
import springboot.model.Usuario;

import java.util.List;

public class ListaQuestoesOutput {
    private String nomeLista;

    private String autor;

    private List<Questao> questoes;

    public ListaQuestoesOutput(String nomeLista, Usuario autor, List<Questao> questoes) {
        this.nomeLista = nomeLista;
        this.autor = autor.getNome();
        this.questoes = questoes;
    }
    public ListaQuestoesOutput(ListaQuestoes listaOriginal) {
        this.nomeLista = listaOriginal.getNomeLista();
        this.autor = listaOriginal.getAutor().getNome();
        this.questoes = listaOriginal.getQuestoes();
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
