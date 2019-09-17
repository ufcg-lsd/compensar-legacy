package springboot.dto.IO;

import springboot.dto.input.ListaQuestoesInput;
import springboot.dto.input.QuestaoInput;
import springboot.dto.output.ListaQuestoesOutput;
import springboot.model.ListaQuestoes;
import springboot.model.Questao;
import springboot.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListaQuestoesIO {
    public static ListaQuestoes convert(ListaQuestoesInput listaQuestoes, Usuario usuario) {
        List<Questao> questoes = new ArrayList<>();
        for (QuestaoInput questao: listaQuestoes.getQuestoes()) {
            questoes.add(
                    new Questao(
                            questao.getTipo(),
                            questao.getConteudo(),
                            questao.getEnunciado(),
                            questao.getFonte(),
                            usuario.getNome(),
                            questao.getEspelho(),
                            questao.getAlternativas(),
                            questao.getCompetencias()
                    )
            );
        }
        return new ListaQuestoes(listaQuestoes.getNomeLista(), usuario, questoes);
    }

    public static ListaQuestoesOutput convert(ListaQuestoes listaQuestoes) {
        return new ListaQuestoesOutput(listaQuestoes);
    }
}
