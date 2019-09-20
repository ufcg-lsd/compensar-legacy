package springboot.dto.IO;

import springboot.dto.input.ListaQuestoesInput;
import springboot.dto.input.QuestaoInput;
import springboot.dto.output.ListaQuestoesOutput;
import springboot.model.ListaQuestoes;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.QuestaoService;

import java.util.ArrayList;
import java.util.List;

public class ListaQuestoesIO {
    public static ListaQuestoes convert(ListaQuestoesInput listaQuestoes, Usuario usuario, QuestaoService questaoService) {
        List<Questao> questoes = new ArrayList<>();
        for (String questao: listaQuestoes.getQuestoes()) {
            questoes.add(questaoService.getById(questao));
        }
        return new ListaQuestoes(listaQuestoes.getNomeLista(), usuario, questoes);
    }

    public static ListaQuestoesOutput convert(ListaQuestoes listaQuestoes) {
        return new ListaQuestoesOutput(listaQuestoes);
    }
}
