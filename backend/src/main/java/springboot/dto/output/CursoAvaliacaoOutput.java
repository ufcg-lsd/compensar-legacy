package springboot.dto.output;

import springboot.model.ModuloCurso;
import springboot.model.Questao;

import java.util.List;

public class CursoAvaliacaoOutput {
    private String competencia;

    private List<ModuloCursoOutput> cursoAvaliacao;

    public CursoAvaliacaoOutput(String competencia, List<ModuloCursoOutput> cursoAvaliacao) {
        this.competencia = competencia;
        this.cursoAvaliacao = cursoAvaliacao;
    }

    public CursoAvaliacaoOutput() {
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public List<ModuloCursoOutput> getCursoAvaliacao() {
        return cursoAvaliacao;
    }

    public void setCursoAvaliacao(List<ModuloCursoOutput> cursoAvaliacao) {
        this.cursoAvaliacao = cursoAvaliacao;
    }
}
