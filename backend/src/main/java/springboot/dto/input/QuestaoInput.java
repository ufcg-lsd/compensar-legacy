package springboot.dto.input;

import springboot.enums.CompetenciaType;
import springboot.model.Alternativa;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * DTO para representar a entrada de dados de uma questão.
 */
public final class QuestaoInput {

    private final String tipo;
    private final String enunciado;
    private final Set<CompetenciaType> competencias;
    private final String fonte;
    private final String espelho;
    private final Set<String> conteudo;
    private final List<Alternativa> alternativas;
    private final Set<CompetenciaType> competenciasAvaliacao;
    private final List<String> infoCompetenciasAvaliacao;
    private final Integer confiancaAvaliacao;
    private final String obsAvaliacao;

    /**
     * Construtor para inicializar um QuestaoInput.
     *
     * @param tipo                      O tipo da questão.
     * @param enunciado                 O enunciado ou descrição da questão.
     * @param competencias              O conjunto de competências associadas à
     *                                  questão.
     * @param fonte                     A fonte ou referência da questão.
     * @param espelho                   A resposta ou explicação detalhada da
     *                                  questão.
     * @param conteudo                  O conjunto de conteúdos relacionados à
     *                                  questão.
     * @param alternativas              A lista de alternativas possíveis para a
     *                                  questão.
     * @param competenciasAvaliacao     As competências atribuídas na avaliação da
     *                                  questão.
     * @param infoCompetenciasAvaliacao Informações adicionais sobre as competências
     *                                  avaliadas.
     * @param confiancaAvaliacao        O nível de confiança na avaliação.
     * @param obsAvaliacao              Observações relacionadas à avaliação da
     *                                  questão.
     * @throws IllegalArgumentException Se algum parâmetro obrigatório for nulo ou
     *                                  inválido.
     */
    public QuestaoInput(String tipo, String enunciado, Set<CompetenciaType> competencias, String fonte,
            String espelho, Set<String> conteudo, List<Alternativa> alternativas,
            Set<CompetenciaType> competenciasAvaliacao, List<String> infoCompetenciasAvaliacao,
            Integer confiancaAvaliacao, String obsAvaliacao) {

        this.tipo = Optional.ofNullable(tipo)
                .filter(t -> !t.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("O tipo não pode ser nulo ou vazio."));
        this.enunciado = Optional.ofNullable(enunciado)
                .filter(e -> !e.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("O enunciado não pode ser nulo ou vazio."));
        this.competencias = Optional.ofNullable(competencias)
                .orElseThrow(() -> new IllegalArgumentException("Competências não podem ser nulas."));
        this.conteudo = Optional.ofNullable(conteudo)
                .map(Collections::unmodifiableSet)
                .orElseThrow(() -> new IllegalArgumentException("Conteúdo não pode ser nulo."));
        this.fonte = fonte;
        this.espelho = espelho;
        this.competenciasAvaliacao = competenciasAvaliacao;
        this.infoCompetenciasAvaliacao = infoCompetenciasAvaliacao;
        this.confiancaAvaliacao = confiancaAvaliacao;
        this.obsAvaliacao = obsAvaliacao;

        if (!"Subjetiva".equalsIgnoreCase(tipo)) {
            this.alternativas = Optional.ofNullable(alternativas)
                    .map(Collections::unmodifiableList)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Alternativas não podem ser nulas para questões objetivas."));
        } else {
            this.alternativas = alternativas; // Pode ser nulo para questões subjetivas
        }
    }

    public String getTipo() {
        return tipo;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public Set<CompetenciaType> getCompetencias() {
        return competencias;
    }

    public String getFonte() {
        return fonte;
    }

    public String getEspelho() {
        return espelho;
    }

    public Set<String> getConteudo() {
        return conteudo;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public Set<CompetenciaType> getCompetenciasAvaliacao() {
        return competenciasAvaliacao;
    }

    public List<String> getInfoCompetenciasAvaliacao() {
        return infoCompetenciasAvaliacao;
    }

    public Integer getConfiancaAvaliacao() {
        return confiancaAvaliacao;
    }

    public String getObsAvaliacao() {
        return obsAvaliacao;
    }
}
