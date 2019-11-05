package springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import springboot.enums.CompetenciaType;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class Avaliacao {
    @Id
    @JsonProperty
    private String id;

    @NotNull
    @TextIndexed
    private String observacaoAvaliacao;

    @TextIndexed
    private String observacaoQuestao;

    @TextIndexed
    private Set<CompetenciaType> competencias;

    private String autor;

    private String questao;

    private Integer confianca;

    public Avaliacao(@NotNull String observacaoAvaliacao, String observacaoQuestao, Set<CompetenciaType> competencias, String autor, String questao, Integer confianca) {
        this.observacaoAvaliacao = observacaoAvaliacao;
        this.observacaoQuestao = observacaoQuestao;
        this.competencias = competencias;
        this.autor = autor;
        this.questao = questao;
        this.confianca = confianca;
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

    public void setObservacaoAvaliacao(String observacaoAvaliacao) {
        this.observacaoAvaliacao = observacaoAvaliacao;
    }

    public String getObservacaoQuestao() {
        return observacaoQuestao;
    }

    public void setObservacaoQuestao(String observacaoQuestao) {
        this.observacaoQuestao = observacaoQuestao;
    }

    public Set<CompetenciaType> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(Set<CompetenciaType> competencias) {
        this.competencias = competencias;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getQuestao() {
        return questao;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public Integer getConfianca() {
        return confianca;
    }

    public void setConfianca(Integer confianca) {
        this.confianca = confianca;
    }
}
