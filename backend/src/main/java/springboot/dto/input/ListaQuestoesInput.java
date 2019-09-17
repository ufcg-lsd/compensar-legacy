package springboot.dto.input;

import java.util.List;

public class ListaQuestoesInput {

    private String nomeLista;
    private List<QuestaoInput> questoes;

    public ListaQuestoesInput(String nomeLista, List<QuestaoInput> questoes) {
        this.nomeLista = nomeLista;
        this.questoes = questoes;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public List<QuestaoInput> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<QuestaoInput> questoes) {
        this.questoes = questoes;
    }
}
