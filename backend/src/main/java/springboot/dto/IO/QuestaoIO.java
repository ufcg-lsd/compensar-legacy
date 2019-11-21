package springboot.dto.IO;

import springboot.dto.input.QuestaoInput;
import springboot.dto.output.QuestaoOutput;
import springboot.enums.AvaliacaoPublicacao;
import springboot.enums.EstadoQuestao;
import springboot.model.Avaliacao;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.AvaliacaoService;
import springboot.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

public class QuestaoIO {

    public static Questao convert(QuestaoInput questao, String autor) {
        return new Questao(questao.getTipo(), questao.getConteudo(), questao.getEnunciado(), questao.getFonte(), autor, questao.getEspelho(), questao.getAlternativas(), questao.getCompetencias());
    }

    public static QuestaoOutput convert(Questao questao, Usuario usuarioAtual, UsuarioService usuarioService, AvaliacaoService avaliacaoService, boolean forceAvaliacoes) {
        List<String> sugestoes = new ArrayList<>();
        List<AvaliacaoPublicacao> avalPublicacoes = new ArrayList<>();
        if ((usuarioAtual.getEmail().equals(questao.getAutor()) && questao.getEstado().equals(EstadoQuestao.REJEITADA)) || forceAvaliacoes) {
            List<Avaliacao> avaliacoes = avaliacaoService.getAllByQuestao(questao.getId());
            for (Avaliacao aval : avaliacoes) {
                if (!aval.getObservacaoQuestao().trim().equals("")) {
                    sugestoes.add(aval.getObservacaoQuestao());
                    avalPublicacoes.add(aval.getAvaliacaoPublicacao());
                }
            }
        }

        return new QuestaoOutput(questao.getId(), questao.getTipo(), questao.getEnunciado(), usuarioService.getById(questao.getAutor()).getNome(), questao.getAutor(), questao.getCompetencias(), questao.getFonte(), questao.getEspelho(), questao.getConteudo(), questao.getAlternativas(), sugestoes, avalPublicacoes, questao.getEstado());
    }
}
