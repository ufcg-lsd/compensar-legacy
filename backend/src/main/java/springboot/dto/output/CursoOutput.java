package springboot.dto.output;

import springboot.model.ModuloCurso;
import springboot.model.Questao;

import java.util.ArrayList;
import java.util.List;

public class CursoOutput {
    private String mensagem;

    private List<ModuloCursoOutput> cursoAvaliacao;

    public CursoOutput(List<ModuloCursoOutput> cursoAvaliacao, String mensagem) {
        this.mensagem = mensagem;
        this.cursoAvaliacao = cursoAvaliacao;
    }

    public CursoOutput() {
        this.cursoAvaliacao = new ArrayList<>();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<ModuloCursoOutput> getCursoAvaliacao() {
        return cursoAvaliacao;
    }

    public void setCursoAvaliacao(List<ModuloCursoOutput> cursoAvaliacao) {
        this.cursoAvaliacao = cursoAvaliacao;
    }
}
