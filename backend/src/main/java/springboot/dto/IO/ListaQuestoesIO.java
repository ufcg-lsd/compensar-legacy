package springboot.dto.IO;

import org.springframework.beans.factory.annotation.Autowired;
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
        List<String> questoes = new ArrayList<>();
        for (String questao: listaQuestoes.getQuestoes()) {
            questoes.add(questaoService.getById(questao).getId());
        }
        return new ListaQuestoes(listaQuestoes.getNomeLista(), usuario.getEmail(), questoes);
    }

    public static ListaQuestoesOutput convert(ListaQuestoes listaQuestoes, String autor, QuestaoService questaoService) {
        List<Questao> questoes = new ArrayList<>();
        for (String questao: listaQuestoes.getQuestoes()) {
            questoes.add(questaoService.getById(questao));
        }
        return new ListaQuestoesOutput(listaQuestoes.getId(), listaQuestoes.getNomeLista(), autor,  questoes);
    }
}
