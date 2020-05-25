package springboot.dto.output;

import springboot.model.ModuloCurso;
import springboot.model.Questao;

import java.util.ArrayList;
import java.util.List;

public class CursoOutput {
    private String mensagem;

    private List<ModuloCursoOutput> cursoAvaliacao;

    private List<ModuloCursoOutput> cursoCriacao;

    public CursoOutput(String mensagem, List<ModuloCursoOutput> cursoAvaliacao, List<ModuloCursoOutput> cursoCriacao) {
        this.mensagem = mensagem;
        this.cursoAvaliacao = cursoAvaliacao;
        this.cursoCriacao = cursoCriacao;
    }

    public CursoOutput() {
        this.cursoAvaliacao = new ArrayList<>();
        this.cursoCriacao = cursoCriacao;
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

    public List<ModuloCursoOutput> getCursoCriacao() {
        return cursoCriacao;
    }

    public void setCursoCriacao(List<ModuloCursoOutput> cursoCriacao) {
        this.cursoCriacao = cursoCriacao;
    }
}
