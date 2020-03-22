package springboot.dto.output;

import springboot.enums.AvaliacaoPublicacao;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;
import springboot.model.Alternativa;

import java.util.List;
import java.util.Set;

public class QuestaoOutput {

    private String id;

    private String tipo;

    private String enunciado;

    private String originalEnunciado;

    public String autor;

    public String emailAutor;

    private Set<CompetenciaType> competencias;

    private List<Set<CompetenciaType> > competenciasAvaliacoes;

    private String fonte;

    private String espelho;

    private Set<String> conteudo;

    private List<Alternativa> alternativas;

    private List<String> sugestoes;

    private List<AvaliacaoPublicacao> avaliacoesPublicacao;

    private EstadoQuestao estado;

    public QuestaoOutput(String id, String tipo, String enunciado, String originalEnunciado, String autor, String emailAutor, Set<CompetenciaType> competencias, List<Set<CompetenciaType> > competenciasAvaliacoes, String fonte, String espelho, Set<String> conteudo, List<Alternativa> alternativas, List<String> sugestoes, List<AvaliacaoPublicacao> avaliacoesPublicacao, EstadoQuestao estado) {
        this.id = id;
        this.tipo = tipo;
        this.enunciado = enunciado;
        this.originalEnunciado = originalEnunciado;
        this.autor = autor;
        this.emailAutor = emailAutor;
        this.competencias = competencias;
        this.competenciasAvaliacoes = competenciasAvaliacoes;
        this.fonte = fonte;
        this.espelho = espelho;
        this.conteudo = conteudo;
        this.alternativas = alternativas;
        this.sugestoes = sugestoes;
        this.avaliacoesPublicacao = avaliacoesPublicacao;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOriginalEnunciado() {
        return originalEnunciado;
    }

    public void setOriginalEnunciado(String originalEnunciado) {
        this.originalEnunciado = originalEnunciado;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEmailAutor() {
        return emailAutor;
    }

    public void setEmailAutor(String emailAutor) {
        this.emailAutor = emailAutor;
    }

    public Set<CompetenciaType> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(Set<CompetenciaType> competencias) {
        this.competencias = competencias;
    }

    public List<Set<CompetenciaType>> getCompetenciasAvaliacoes() {
        return competenciasAvaliacoes;
    }

    public void setCompetenciasAvaliacoes(List<Set<CompetenciaType>> competenciasAvaliacoes) {
        this.competenciasAvaliacoes = competenciasAvaliacoes;
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

    public Set<String> getConteudo() {
        return conteudo;
    }

    public void setConteudo(Set<String> conteudo) {
        this.conteudo = conteudo;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public List<String> getSugestoes() {
        return sugestoes;
    }

    public void setSugestoes(List<String> sugestoes) {
        this.sugestoes = sugestoes;
    }

    public List<AvaliacaoPublicacao> getAvaliacoesPublicacao() {
        return avaliacoesPublicacao;
    }

    public void setAvaliacoesPublicacao(List<AvaliacaoPublicacao> avaliacoesPublicacao) {
        this.avaliacoesPublicacao = avaliacoesPublicacao;
    }

    public EstadoQuestao getEstado() {
        return estado;
    }

    public void setEstado(EstadoQuestao estado) {
        this.estado = estado;
    }
}
