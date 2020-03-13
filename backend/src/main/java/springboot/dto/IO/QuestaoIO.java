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
        List<String> conteudo = new ArrayList<>();
        String originalConteudo = null;
        Set<CompetenciaType> competenciasAutor = new HashSet<>();
        Set<CompetenciaType> competenciasClassificador = new HashSet<>();
        List<AvaliacaoPublicacao> avalPublicacoes = new ArrayList<>();
        if ((usuarioAtual.getEmail().equals(questao.getAutor()) && questao.getEstado().equals(EstadoQuestao.REJEITADA)) || forceAvaliacoes) {
            List<Avaliacao> avaliacoes = avaliacaoService.getAllByQuestao(questao.getId());
            for (Avaliacao aval : avaliacoes) {
                avalPublicacoes.add(aval.getAvaliacaoPublicacao());
                String tmp = "";
                if (!aval.getObservacaoQuestao().trim().equals("")) {
                    tmp += aval.getObservacaoQuestao();
                }
                if (aval.getAvaliacaoPublicacao().equals(AvaliacaoPublicacao.PRONTA)) {
                    tmp += "<strong>(Pronta para publicação)</strong>";
                } else if (aval.getAvaliacaoPublicacao().equals(AvaliacaoPublicacao.PEQUENAS_ALTERACOES)) {
                    tmp += "<strong>(Necessita de pequenas alterações)</strong>";
                } else if (aval.getAvaliacaoPublicacao().equals(AvaliacaoPublicacao.MUITAS_ALTERACOES)) {
                    tmp += "<strong>(Necessita de muitas alterações)</strong>";
                } else {
                    tmp += "<strong>(Fora de contexto)</strong>";
                }
                sugestoes.add(tmp);
            }
        }
        if (usuarioAtual.getEmail().equals(questao.getAutor()) && questao.getEstado() != EstadoQuestao.RASCUNHO) {
            originalConteudo = questao.getOriginalEnunciado();
        }

        if (usuarioAtual.getEmail().equals(questao.getAutor())) {

            List<Avaliacao> avaliacoesQuestao = avaliacaoService.getAllByQuestao(questao.getId());
            for (Avaliacao av : avaliacoesQuestao) {
                if (av.getAutor().equals(usuarioAtual.getEmail())) {
                    competenciasAutor = av.getCompetencias();
                }
            }

            competenciasClassificador = questao.getCompetenciasClassificador();
        }
        return new QuestaoOutput(questao.getId(), questao.getTipo(), questao.getEnunciado(), originalConteudo, usuarioService.getById(questao.getAutor()).getNome(), questao.getAutor(), questao.getCompetencias(), competenciasAutor, competenciasClassificador, questao.getFonte(), questao.getEspelho(), conteudo, questao.getAlternativas(), sugestoes, avalPublicacoes, questao.getEstado());
    }
}
