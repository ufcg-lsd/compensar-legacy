package springboot.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

/**
 * DTO para representar a entrada de dados de um usuário.
 */
public final class UsuarioInput {

    @JsonProperty("cargo")
    private final String cargo;

    @JsonProperty("cidade")
    private final String cidade;

    @JsonProperty("idade")
    private final int idade;

    @JsonProperty("nomeInstituicao")
    private final String nomeInstituicao;

    /**
     * Construtor para inicializar um UsuarioInput.
     *
     * @param cargo           O cargo ou função do usuário.
     * @param cidade          A cidade de residência do usuário.
     * @param idade           A idade do usuário.
     * @param nomeInstituicao O nome da instituição associada ao usuário.
     * @throws IllegalArgumentException Se algum parâmetro obrigatório for nulo ou
     *                                  inválido.
     */
    public UsuarioInput(String cargo, String cidade, int idade, String nomeInstituicao) {
        this.cargo = Optional.ofNullable(cargo)
                .filter(c -> !c.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("O cargo não pode ser nulo ou vazio."));
        this.cidade = Optional.ofNullable(cidade)
                .filter(c -> !c.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("A cidade não pode ser nula ou vazia."));
        if (idade < 0) {
            throw new IllegalArgumentException("A idade não pode ser negativa.");
        }
        this.idade = idade;
        this.nomeInstituicao = Optional.ofNullable(nomeInstituicao)
                .filter(n -> !n.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("O nome da instituição não pode ser nulo ou vazio."));
    }

    public String getCargo() {
        return cargo;
    }

    public String getCidade() {
        return cidade;
    }

    public int getIdade() {
        return idade;
    }

    public String getNomeInstituicao() {
        return nomeInstituicao;
    }
}
