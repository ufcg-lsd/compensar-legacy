package springboot.dto.input;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import springboot.enums.AvaliacaoPublicacao;
import springboot.enums.CompetenciaType;

/**
 * DTO para entrada de dados de Avaliação.
 */
public final class AvaliacaoInput {

    private final String observacaoAvaliacao;
    private final String observacaoQuestao;
    private final String questaoId;
    private final Set<CompetenciaType> competencias;
    private final List<String> infoCompetencias;
    private final Integer confianca;
    private final AvaliacaoPublicacao avaliacaoPublicacao;

    /**
     * Construtor para inicializar todos os campos da Avaliação.
     *
     * @param observacaoAvaliacao Observação geral sobre a avaliação.
     * @param observacaoQuestao   Observação específica sobre a questão.
     * @param questaoId           Identificador da questão a ser avaliada.
     * @param competencias        Competências aplicáveis à avaliação.
     * @param infoCompetencias    Informações adicionais sobre as competências.
     * @param confianca           Nível de confiança na avaliação (deve estar entre
     *                            0 e 100).
     * @param avaliacaoPublicacao Status de publicação da avaliação.
     */
    public AvaliacaoInput(String observacaoAvaliacao,
            String observacaoQuestao,
            String questaoId,
            Set<CompetenciaType> competencias,
            List<String> infoCompetencias,
            Integer confianca,
            AvaliacaoPublicacao avaliacaoPublicacao) {

        this.observacaoAvaliacao = Optional.ofNullable(observacaoAvaliacao).orElse("");
        this.observacaoQuestao = Optional.ofNullable(observacaoQuestao).orElse("");
        this.questaoId = Optional.ofNullable(questaoId)
                .filter(id -> !id.trim().isEmpty())
                .orElseThrow(
                        () -> new IllegalArgumentException("O identificador da questão não pode ser nulo ou vazio."));
        this.competencias = Optional.ofNullable(competencias)
                .filter(comp -> !comp.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Competências não podem ser nulas ou vazias."));
        this.infoCompetencias = Optional.ofNullable(infoCompetencias).orElse(Arrays.asList());
        this.confianca = Optional.ofNullable(confianca)
                .filter(conf -> conf >= 0 && conf <= 100)
                .orElseThrow(() -> new IllegalArgumentException("Confianca deve estar entre 0 e 100."));
        this.avaliacaoPublicacao = Optional.ofNullable(avaliacaoPublicacao)
                .orElseThrow(() -> new IllegalArgumentException("AvaliacaoPublicacao não pode ser nula."));
    }

    public String getObservacaoAvaliacao() {
        return observacaoAvaliacao;
    }

    public String getObservacaoQuestao() {
        return observacaoQuestao;
    }

    public String getQuestaoId() {
        return questaoId;
    }

    public Set<CompetenciaType> getCompetencias() {
        return competencias;
    }

    public List<String> getInfoCompetencias() {
        return infoCompetencias;
    }

    public Integer getConfianca() {
        return confianca;
    }

    public AvaliacaoPublicacao getAvaliacaoPublicacao() {
        return avaliacaoPublicacao;
    }
}
