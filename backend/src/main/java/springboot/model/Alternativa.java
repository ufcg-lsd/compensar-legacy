package springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * Classe responsavel por conter metodos e atributos de um objeto do tipo Alternativa.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino 
 */

@Entity(name = "Alternativa")
public class Alternativa {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_alternativa", updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String texto;

	@Column(nullable = false)
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
	 * Recupera o id da alternativa.
	 *
	 * @return O id da alternativa.
	 */
	public Long getId_alternativa() {
		return id;
	}

	/**
	 * @param id_alternativa
	 *            O id da alternativa
	 *
	 *            Atualiza o id da alternativa
	 *
	 */
	public void setId_alternativa(Long id_alternativa) {
		this.id = id_alternativa;
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
