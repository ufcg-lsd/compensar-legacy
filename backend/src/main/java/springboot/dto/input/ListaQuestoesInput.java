package springboot.dto.input;

import java.util.List;

public class ListaQuestoesInput {

    private String nomeLista;
    private List<String> questoes;

    public ListaQuestoesInput(String nomeLista, List<String> questoes) {
        this.nomeLista = nomeLista;
        this.questoes = questoes;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public List<String> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<String> questoes) {
        this.questoes = questoes;
    }
}
