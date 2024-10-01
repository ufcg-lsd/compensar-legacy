package springboot.dto.input;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * DTO para representar a entrada de dados de uma lista de questões.
 */
public final class ListaQuestoesInput {

    private final String nomeLista;
    private final List<String> questoes;

    /**
     * Construtor para inicializar uma ListaQuestoesInput.
     *
     * @param nomeLista O nome da lista de questões.
     * @param questoes  A lista de identificadores de questões. Não pode ser nula ou
     *                  vazia.
     * @throws IllegalArgumentException Se nomeLista for nulo ou vazio, ou se
     *                                  questoes for nulo ou vazio.
     */
    public ListaQuestoesInput(String nomeLista, List<String> questoes) {
        this.nomeLista = Optional.ofNullable(nomeLista)
                .filter(nome -> !nome.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Nome da lista não pode ser nulo ou vazio."));
        this.questoes = Optional.ofNullable(questoes)
                .filter(q -> !q.isEmpty())
                .map(Collections::unmodifiableList)
                .orElseThrow(() -> new IllegalArgumentException("A lista de questões não pode ser nula ou vazia."));
    }

    /**
     * Obtém o nome da lista de questões.
     *
     * @return O nome da lista.
     */
    public String getNomeLista() {
        return nomeLista;
    }

    /**
     * Obtém a lista de identificadores de questões.
     *
     * @return A lista de identificadores de questões.
     */
    public List<String> getQuestoes() {
        return questoes;
    }
}
