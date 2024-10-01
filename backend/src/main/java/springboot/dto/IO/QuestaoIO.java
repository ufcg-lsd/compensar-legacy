package springboot.dto.IO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import springboot.dto.input.QuestaoInput;
import springboot.dto.output.QuestaoOutput;
import springboot.enums.AvaliacaoPublicacao;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;
import springboot.model.Avaliacao;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.AvaliacaoService;
import springboot.service.UsuarioService;

/**
 * Classe responsável pela conversão entre DTOs e entidades de Questão.
 */
@Component
public class QuestaoIO {
    private final UsuarioService usuarioService;
    private final AvaliacaoService avaliacaoService;

    @Autowired
    public QuestaoIO(UsuarioService usuarioService, AvaliacaoService avaliacaoService) {
        this.usuarioService = usuarioService;
        this.avaliacaoService = avaliacaoService;
    }

    /**
     * Converte um DTO de entrada para uma entidade de Questão.
     *
     * @param questaoInput Objeto DTO de entrada.
     * @param autor        E-mail do autor da questão.
     * @return Questao Entidade de modelo.
     */
    public static Questao toEntity(QuestaoInput questaoInput, String autor) {
        return new Questao(
                questaoInput.getTipo(),
                questaoInput.getConteudo(),
                questaoInput.getEnunciado(),
                questaoInput.getFonte(),
                autor,
                questaoInput.getEspelho(),
                questaoInput.getAlternativas(),
                questaoInput.getCompetencias());
    }

    /**
     * Converte uma entidade de Questão para um DTO de saída.
     *
     * @param questao         Entidade de Questão.
     * @param usuarioAtual    Usuário atualmente logado.
     * @param forceAvaliacoes Flag para forçar a inclusão de todas as avaliações.
     * @return QuestaoOutput DTO de saída.
     */
    public QuestaoOutput toDto(Questao questao, Usuario usuarioAtual, boolean forceAvaliacoes) {
        List<String> sugestoes = new ArrayList<>();
        String originalEnunciado = null;
        List<Set<CompetenciaType>> competenciasAvaliacoes = new ArrayList<>();
        List<AvaliacaoPublicacao> avalPublicacoes = new ArrayList<>();

        boolean isAutorOuForce = usuarioAtual.getEmail().equals(questao.getAutor()) &&
                (questao.getEstado() == EstadoQuestao.REJEITADA || questao.getEstado() == EstadoQuestao.PUBLICADA) ||
                forceAvaliacoes;

        if (isAutorOuForce)
            processarAvaliacoes(questao, sugestoes, avalPublicacoes, competenciasAvaliacoes);

        if (usuarioAtual.getEmail().equals(questao.getAutor()) &&
                questao.getEstado() == EstadoQuestao.PUBLICADA &&
                !questao.getEnunciado().equals(questao.getOriginalEnunciado())) {
            originalEnunciado = questao.getOriginalEnunciado();
        }


        String autorNome = Optional.ofNullable(usuarioService.getById(questao.getAutor()))
                .map(Usuario::getNome)
                .orElse("Autor Desconhecido");

        return new QuestaoOutput(
                questao.getId(),
                questao.getTipo(),
                questao.getEnunciado(),
                originalEnunciado,
                autorNome,
                questao.getAutor(),
                questao.getCompetencias(),
                competenciasAvaliacoes,
                questao.getFonte(),
                questao.getEspelho(),
                questao.getConteudo(),
                questao.getAlternativas(),
                sugestoes,
                avalPublicacoes,
                questao.getEstado());
    }

    private void processarAvaliacoes(Questao questao, List<String> sugestoes, List<AvaliacaoPublicacao> avalPublicacoes,
            List<Set<CompetenciaType>> competenciasAvaliacoes) {
        List<Avaliacao> avaliacoes = avaliacaoService.getAllByQuestao(questao.getId());

        avaliacoes.stream().limit(3).forEach(aval -> {
            avalPublicacoes.add(aval.getAvaliacaoPublicacao());
            String sugestao = obterSugestao(aval);
            sugestoes.add(sugestao);
            competenciasAvaliacoes.add(aval.getCompetencias());
        });
        competenciasAvaliacoes.add(questao.getCompetenciasClassificador());
    }

    private String obterSugestao(Avaliacao avaliacao) {
        String sugestaoBase = avaliacao.getObservacaoQuestao().trim().isEmpty() ? "Sem sugestões"
                : avaliacao.getObservacaoQuestao();
        return switch (avaliacao.getAvaliacaoPublicacao()) {
            case PRONTA -> sugestaoBase.concat(" <strong>(Pronta para publicação)</strong>");
            case PEQUENAS_ALTERACOES -> sugestaoBase.concat(" <strong>(Necessita de pequenas alterações)</strong>");
            case MUITAS_ALTERACOES -> sugestaoBase.concat(" <strong>(Necessita de muitas alterações)</strong>");
            default -> sugestaoBase.concat(" <strong>(Fora de contexto)</strong>");
        };
    }
}
