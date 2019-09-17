package springboot.dto.input;

import springboot.enums.CompetenciaType;
import springboot.model.Alternativa;

import java.util.List;
import java.util.Set;

public class QuestaoInput {

    private String tipo;

    private String enunciado;

    private Set<CompetenciaType> competencias;

    private String fonte;

    private String espelho;

    private String conteudo;

    private List<Alternativa> alternativas;

    public QuestaoInput(String tipo, String enunciado, Set<CompetenciaType> competencias, String fonte, String espelho, String conteudo, List<Alternativa> alternativas) {
        this.tipo = tipo;
        this.enunciado = enunciado;
        this.competencias = competencias;
        this.fonte = fonte;
        this.espelho = espelho;
        this.conteudo = conteudo;
        this.alternativas = alternativas;
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

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }
}
