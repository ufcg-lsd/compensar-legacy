package springboot.model;



import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Classe responsavel por conter metodos e atributos de um objeto do tipo Alternativa.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino 
 */


@Document(collection = "alternativa")
public class Alternativa {


	@NotNull
	@Indexed
	private String texto;

	@NotNull
	@Indexed
	private boolean correta;
	
	
	/**
	 * Cria uma alternativa com texto e informação se é correta.
	 *
	 * @param texto
	 *            O texto da alternativa.
	 * @param correta
	 *            A veracidade da alternativa.
	 * 
	 */
	public Alternativa(String texto, boolean correta) {
		this.texto = texto;
		this.correta = correta;
	}

	public Alternativa() {

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
	 * @param texto
	 *            O texto da alternativa.
	 *
	 *            Atualiza o texto da alternativa.
	 *
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

	/**
	 * Recupera a veracidade da alternativa.
	 *
	 * @return O valor true ou false da veraciadade da alternativa.
	 */
	public boolean isCorreta() {
		return correta;
	}
	/**
	 * @param correta
	 *            O valor true ou false da veraciadade da alternativa.
	 *
	 *            Atualiza a veracidade da alternativa.
	 *
	 */
	public void setCorreta(boolean correta) {
		this.correta = correta;
	}
	/**
	 * Representacao em string da alternativa.
	 *
	 * @return A representacao em string da alternativa.
	 */
	@Override
	public String toString() {
		return "Alternativa [texto=" + texto + ", correta=" + correta + "]";
	}

}
