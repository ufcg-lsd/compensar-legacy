package springboot.dto.input;

import springboot.enums.CompetenciaType;

import java.util.List;
import java.util.Set;

public class AvaliacaoInput {
    private String observacoes;

    private String questao;

    private Set<CompetenciaType> competencias;

    private Integer confianca;

    public AvaliacaoInput(String observacoes, String questao, Set<CompetenciaType> competencias, Integer confianca) {
        this.observacoes = observacoes;
        this.questao = questao;
        this.competencias = competencias;
        this.confianca = confianca;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getQuestao() {
        return questao;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public Set<CompetenciaType> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(Set<CompetenciaType> competencias) {
        this.competencias = competencias;
    }

    public Integer getConfianca() {
        return confianca;
    }

    public void setConfianca(Integer confianca) {
        this.confianca = confianca;
    }
}
