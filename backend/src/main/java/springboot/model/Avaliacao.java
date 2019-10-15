package springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import springboot.enums.CompetenciaType;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class Avaliacao {
    @Id
    @JsonProperty
    private String id;

    @NotNull
    @TextIndexed
    private String observacoes;

    @TextIndexed
    private Set<CompetenciaType> competencias;

    @ManyToOne
    private Usuario autor;

    @ManyToOne
    private Questao questao;

    public Avaliacao(@NotNull String observacoes, Set<CompetenciaType> competencias, Usuario autor, Questao questao) {
        this.observacoes = observacoes;
        this.competencias = competencias;
        this.autor = autor;
        this.questao = questao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Set<CompetenciaType> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(Set<CompetenciaType> competencias) {
        this.competencias = competencias;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }
}
