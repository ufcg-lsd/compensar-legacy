package springboot.dto.IO;

import springboot.dto.input.QuestaoInput;
import springboot.dto.output.QuestaoOutput;
import springboot.enums.AvaliacaoPublicacao;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;
import springboot.model.Avaliacao;
import springboot.model.Conteudo;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.AvaliacaoService;
import springboot.service.QuestaoService;
import springboot.service.UsuarioService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestaoIO {

    public static Questao convert(QuestaoInput questao, String autor) {
        return new Questao(questao.getTipo(), questao.getConteudo(), questao.getEnunciado(), questao.getFonte(), autor, questao.getEspelho(), questao.getAlternativas(), questao.getCompetencias());
    }

    public static QuestaoOutput convert(Questao questao, Usuario usuarioAtual, UsuarioService usuarioService, AvaliacaoService avaliacaoService, QuestaoService questaoService, boolean forceAvaliacoes) {
        List<String> sugestoes = new ArrayList<>();
        String originalEnunciado = null;
        List<Set<CompetenciaType> > competenciasAvaliacoes = new ArrayList<>();
        List<AvaliacaoPublicacao> avalPublicacoes = new ArrayList<>();
        if ((usuarioAtual.getEmail().equals(questao.getAutor()) && questao.getEstado().equals(EstadoQuestao.REJEITADA)) || forceAvaliacoes) {
            List<Avaliacao> avaliacoes = avaliacaoService.getAllByQuestao(questao.getId()).subList(1, 3);
            for (Avaliacao aval : avaliacoes) {
                avalPublicacoes.add(aval.getAvaliacaoPublicacao());
                String tmp = aval.getObservacaoQuestao().trim().equals("") ? "Sem sugestões" : aval.getObservacaoQuestao();
                if (aval.getAvaliacaoPublicacao().equals(AvaliacaoPublicacao.PRONTA)) {
                    tmp += " <strong>(Pronta para publicação)</strong>";
                } else if (aval.getAvaliacaoPublicacao().equals(AvaliacaoPublicacao.PEQUENAS_ALTERACOES)) {
                    tmp += " <strong>(Necessita de pequenas alterações)</strong>";
                } else if (aval.getAvaliacaoPublicacao().equals(AvaliacaoPublicacao.MUITAS_ALTERACOES)) {
                    tmp += " <strong>(Necessita de muitas alterações)</strong>";
                } else {
                    tmp += " <strong>(Fora de contexto)</strong>";
                }
                sugestoes.add(tmp);
            }

            competenciasAvaliacoes.add(questao.getCompetenciasClassificador());

            List<Avaliacao> avaliacoesQuestao = avaliacaoService.getAllByQuestao(questao.getId()).subList(0, 3);
            for (Avaliacao av : avaliacoesQuestao) {
                competenciasAvaliacoes.add(av.getCompetencias());
            }
        }
        if (usuarioAtual.getEmail().equals(questao.getAutor()) && questao.getEstado() == EstadoQuestao.PUBLICADA && !questao.getEnunciado().equals(questao.getOriginalEnunciado())) {
            originalEnunciado = questao.getOriginalEnunciado();
        }

        return new QuestaoOutput(questao.getId(), questao.getTipo(), questao.getEnunciado(), originalEnunciado, usuarioService.getById(questao.getAutor()).getNome(), questao.getAutor(), questao.getCompetencias(), competenciasAvaliacoes, questao.getFonte(), questao.getEspelho(), questao.getConteudo(), questao.getAlternativas(), sugestoes, avalPublicacoes, questao.getEstado());
    }
}
