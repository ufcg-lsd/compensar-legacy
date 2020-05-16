package springboot.model;

import java.util.ArrayList;
import java.util.List;

public class ModuloCurso {

    private String nome;

    private EstadoModulo estado;

    private Integer erros;

    private List<String> questoes; // Questões que foram carregadas para receber resposta

    private int numeroExemplo; //Indica qual é o video de exemplo atual

    public ModuloCurso(String nome) {
        this.nome = nome;
        this.estado = EstadoModulo.DESCRICAO;
        this.erros = 0;
        this.questoes = new ArrayList<>();
        this.numeroExemplo = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EstadoModulo getEstado() {
        return estado;
    }

    public void setEstado(EstadoModulo estado) {
        this.estado = estado;
    }

    public Integer getErros() {
        return erros;
    }

    public void setErros(Integer erros) {
        this.erros = erros;
    }

    public void addErro() {
        this.erros++;
    }

    public void zeraErros() {
        this.erros = 0;
    }

    public List<String> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<String> questoes) {
        this.questoes = questoes;
    }

    public int getNumeroExemplo() {
        return numeroExemplo;
    }

    public void setNumeroExemplo(int numeroExemplo) {
        this.numeroExemplo = numeroExemplo;
    }

    public enum EstadoModulo {
        INATIVO,
        DESCRICAO,
        EXEMPLOS,
        PRATICA,
        FINALIZADO;
    }
}
