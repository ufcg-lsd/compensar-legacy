package springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "competencia")
public class Competencia {
    @Id
    private String nome;

    private Curso cursoAvaliacao;

    private Curso cursoCriacao;

    public Competencia(String nome, Curso cursoAvaliacao, Curso cursoCriacao) {
        this.nome = nome;
        this.cursoAvaliacao = cursoAvaliacao;
        this.cursoCriacao = cursoCriacao;
    }

    public Competencia() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Curso getCursoAvaliacao() {
        return cursoAvaliacao;
    }

    public void setCursoAvaliacao(Curso cursoAvaliacao) {
        this.cursoAvaliacao = cursoAvaliacao;
    }

    public Curso getCursoCriacao() {
        return cursoCriacao;
    }

    public void setCursoCriacao(Curso cursoCriacao) {
        this.cursoCriacao = cursoCriacao;
    }
}