package springboot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um módulo de curso, com seu estado, questões associadas
 * e erros.
 */
public class ModuloCurso {

    private String nome;
    private EstadoModulo estado;
    private int erros;
    private List<String> questoes; // IDs das questões
    private int numeroExemplo; // Video de exemplo atual (pode não existir)

    /**
     * Construtor para inicializar o módulo do curso com um nome.
     * 
     * @param nome O nome do módulo.
     */
    public ModuloCurso(String nome) {
        this.nome = nome;
        this.estado = EstadoModulo.DESCRICAO;
        this.erros = 0;
        this.questoes = new ArrayList<>();
        this.numeroExemplo = -1;
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

    public int getErros() {
        return erros;
    }

    public void incrementarErros() {
        this.erros++;
    }

    public void resetarErros() {
        this.erros = 0;
    }

    public List<String> getQuestoes() {
        return new ArrayList<>(questoes);
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
        FINALIZADO
    }
}
