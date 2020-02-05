package springboot.dto.input;

import springboot.enums.CompetenciaType;
import springboot.model.Alternativa;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuestaoInput {

    private String tipo;

    private String enunciado;

    private Set<CompetenciaType> competencias;

    private String fonte;

    private String espelho;

    private List<String> conteudo;

    private List<Alternativa> alternativas;

    private Set<CompetenciaType> competenciasAvaliacao;

    private List<String> infoCompetenciasAvaliacao;

    private Integer confiancaAvaliacao;

    private String obsAvaliacao;

    public QuestaoInput(String tipo, String enunciado, Set<CompetenciaType> competencias, String fonte, String espelho, List<String> conteudo, List<Alternativa> alternativas, Set<CompetenciaType> competenciasAvaliacao, List<String> infoCompetenciasAvaliacao, Integer confiancaAvaliacao, String obsAvaliacao) {
        this.tipo = tipo;
        this.enunciado = enunciado;
        this.competencias = competencias;
        this.fonte = fonte;
        this.espelho = espelho;
        this.conteudo = conteudo;
        this.alternativas = alternativas;
        this.competenciasAvaliacao = competenciasAvaliacao;
        this.infoCompetenciasAvaliacao = infoCompetenciasAvaliacao;
        this.confiancaAvaliacao = confiancaAvaliacao;
        this.obsAvaliacao = obsAvaliacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Set<CompetenciaType> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(Set<CompetenciaType> competencias) {
        this.competencias = competencias;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getEspelho() {
        return espelho;
    }

    public void setEspelho(String espelho) {
        this.espelho = espelho;
    }

    public List<String> getConteudo() {
        return conteudo;
    }

    public void setConteudo(List<String> conteudo) {
        this.conteudo = conteudo;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public Set<CompetenciaType> getCompetenciasAvaliacao() {
        return competenciasAvaliacao;
    }

    public void setCompetenciasAvaliacao(Set<CompetenciaType> competenciasAvaliacao) {
        this.competenciasAvaliacao = competenciasAvaliacao;
    }

    public List<String> getInfoCompetenciasAvaliacao() {
        return infoCompetenciasAvaliacao;
    }

    public void setInfoCompetenciasAvaliacao(List<String> infoCompetenciasAvaliacao) {
        this.infoCompetenciasAvaliacao = infoCompetenciasAvaliacao;
    }

    public Integer getConfiancaAvaliacao() {
        return confiancaAvaliacao;
    }

    public void setConfiancaAvaliacao(Integer conviancaAvaliacao) {
        this.confiancaAvaliacao = conviancaAvaliacao;
    }

    public String getObsAvaliacao() {
        return obsAvaliacao;
    }

    public void setObsAvaliacao(String obsAvaliacao) {
        this.obsAvaliacao = obsAvaliacao;
    }
}
