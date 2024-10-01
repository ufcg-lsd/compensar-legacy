package springboot.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import springboot.enums.AvaliacaoPublicacao;
import springboot.enums.CompetenciaType;

public class Avaliacao {

    @Id
    @JsonProperty
    private String id;

    @NotNull
    @TextIndexed
    @NotBlank(message = "A observação da avaliação não pode estar em branco")
    private final String observacaoAvaliacao;

    @TextIndexed
    private final String observacaoQuestao;

    @TextIndexed
    private final Set<CompetenciaType> competencias;

    private final List<String> infoCompetencias;

    @NotBlank(message = "O autor não pode estar em branco")
    private final String autor;

    @NotBlank(message = "A questão não pode estar em branco")
    private final String questao;

    private final Integer confianca;

    @NotNull(message = "A publicação da avaliação não pode ser nula")
    private final AvaliacaoPublicacao avaliacaoPublicacao;

    // Construtor imutável
    public Avaliacao(String observacaoAvaliacao, String observacaoQuestao, Set<CompetenciaType> competencias,
            List<String> infoCompetencias, String autor, String questao, Integer confianca,
            AvaliacaoPublicacao avaliacaoPublicacao) {
        this.observacaoAvaliacao = observacaoAvaliacao;
        this.observacaoQuestao = observacaoQuestao;
        this.competencias = competencias;
        this.infoCompetencias = infoCompetencias;
        this.autor = autor;
        this.questao = questao;
        this.confianca = confianca;
        this.avaliacaoPublicacao = avaliacaoPublicacao;
        this.id = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObservacaoAvaliacao() {
        return observacaoAvaliacao;
    }

    public String getObservacaoQuestao() {
        return observacaoQuestao;
    }

    public Set<CompetenciaType> getCompetencias() {
        return competencias;
    }

    public List<String> getInfoCompetencias() {
        return infoCompetencias;
    }

    public String getAutor() {
        return autor;
    }

    public String getQuestao() {
        return questao;
    }

    public Integer getConfianca() {
        return confianca;
    }

    public AvaliacaoPublicacao getAvaliacaoPublicacao() {
        return avaliacaoPublicacao;
    }

    @Override
    public String toString() {
        return "Avaliacao{".concat(
                "id='").concat(id).concat("', observacaoAvaliacao='").concat(observacaoAvaliacao)
                .concat("', observacaoQuestao='").concat(observacaoQuestao).concat("', competencias=")
                .concat(competencias.toString()).concat(", infoCompetencias=").concat(infoCompetencias.toString())
                .concat(", autor='")
                .concat(autor).concat("', questao='").concat(questao).concat("', confianca=")
                .concat(confianca.toString())
                .concat(", avaliacaoPublicacao=").concat(avaliacaoPublicacao.toString()).concat("}");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Avaliacao avaliacao = (Avaliacao) o;

        if (!id.equals(avaliacao.id))
            return false;
        if (!observacaoAvaliacao.equals(avaliacao.observacaoAvaliacao))
            return false;
        if (!observacaoQuestao.equals(avaliacao.observacaoQuestao))
            return false;
        if (!competencias.equals(avaliacao.competencias))
            return false;
        if (!infoCompetencias.equals(avaliacao.infoCompetencias))
            return false;
        if (!autor.equals(avaliacao.autor))
            return false;
        if (!questao.equals(avaliacao.questao))
            return false;
        if (!confianca.equals(avaliacao.confianca))
            return false;
        return avaliacaoPublicacao == avaliacao.avaliacaoPublicacao;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + observacaoAvaliacao.hashCode();
        result = 31 * result + observacaoQuestao.hashCode();
        result = 31 * result + competencias.hashCode();
        result = 31 * result + infoCompetencias.hashCode();
        result = 31 * result + autor.hashCode();
        result = 31 * result + questao.hashCode();
        result = 31 * result + confianca.hashCode();
        result = 31 * result + avaliacaoPublicacao.hashCode();
        return result;
    }
}
