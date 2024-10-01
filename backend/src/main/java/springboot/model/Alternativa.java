package springboot.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Classe responsável por conter métodos e atributos de um objeto do tipo
 * Alternativa.
 *
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino
 */
public class Alternativa {

	@NotBlank(message = "O texto da alternativa não pode estar vazio")
	private final String texto;

	@NotNull(message = "A veracidade da alternativa deve ser informada")
	private final Boolean correta;

	/**
	 * Cria uma alternativa com texto e informação se é correta.
	 *
	 * @param texto   O texto da alternativa.
	 * @param correta A veracidade da alternativa.
	 */
	public Alternativa(String texto, Boolean correta) {
		this.texto = texto;
		this.correta = correta;
	}

	/**
	 * Recupera o texto da alternativa.
	 *
	 * @return O texto da alternativa.
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Recupera a veracidade da alternativa.
	 *
	 * @return O valor true ou false da veracidade da alternativa.
	 */
	public Boolean isCorreta() {
		return correta;
	}

	/**
	 * Representação em string da alternativa.
	 *
	 * @return A representação em string da alternativa.
	 */
	@Override
	public String toString() {
		return "Alternativa [texto=".concat(texto).concat(", correta=").concat(String.valueOf(correta)).concat("]");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Alternativa that = (Alternativa) o;

		if (!texto.equals(that.texto))
			return false;
		return correta.equals(that.correta);
	}

	@Override
	public int hashCode() {
		int result = texto.hashCode();
		result = 31 * result + correta.hashCode();
		return result;
	}
}
