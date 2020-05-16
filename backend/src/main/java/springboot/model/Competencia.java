package springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "competencia")
public class Competencia {
    @Id
    private String nome;

    private Curso cursoAvaliacao;

    public Competencia(String nome, Curso cursoAvaliacao) {
        this.nome = nome;
        this.cursoAvaliacao = cursoAvaliacao;
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
}