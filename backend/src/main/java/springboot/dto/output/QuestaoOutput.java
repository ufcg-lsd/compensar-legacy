package springboot.dto.output;

<<<<<<< Updated upstream
import springboot.enums.CompetenciaType;
import springboot.model.Alternativa;

import java.util.List;
import java.util.Set;

=======
>>>>>>> Stashed changes
public class QuestaoOutput {

    private String id;

    private String tipo;

    private String enunciado;

<<<<<<< Updated upstream
    public String autor;

    public String emailAutor;

    private Set<CompetenciaType> competencias;

    private String fonte;

    private String espelho;

    private String conteudo;

    private List<Alternativa> alternativas;

    private List<String> sugestoes;

    public QuestaoOutput(String id, String tipo, String enunciado, String autor, String emailAutor, Set<CompetenciaType> competencias, String fonte, String espelho, String conteudo, List<Alternativa> alternativas, List<String> sugestoes) {
        this.id = id;
        this.tipo = tipo;
        this.enunciado = enunciado;
        this.autor = autor;
        this.emailAutor = emailAutor;
        this.competencias = competencias;
        this.fonte = fonte;
        this.espelho = espelho;
        this.conteudo = conteudo;
        this.alternativas = alternativas;
        this.sugestoes = sugestoes;
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

    public List<String> getSugestoes() {
        return sugestoes;
    }

    public void setSugestoes(List<String> sugestoes) {
        this.sugestoes = sugestoes;
    }
=======


>>>>>>> Stashed changes
}
