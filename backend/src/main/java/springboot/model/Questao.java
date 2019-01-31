package springboot.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

/**
 * Classe abstrata que representa um molde de uma Questão, que pode ser Objetiva ou Subjetiva.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino 
 */

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import springboot.enums.CompetenciaType;

@Document(collection = "questao")
public class Questao {

	@Id
	@JsonProperty
	private String id;

	@NotNull
	private String tipo;

	@NotNull
	@TextIndexed
	private String enunciado;

	private String fonte;

	private String autor;

	private String espelho;

	private List<Alternativa> alternativas;

	private Set<CompetenciaType> competencias;

	/**
	 * Cria uma Questão com tipo, enunciado, fonte, autor e imagem.
	 *
	 * @param tipo      O tipo da questão.
	 * @param enunciado O enunciado da questão.
	 * @param fonte     A fonte da questão.
	 * @param autor     O autor da questão.
	 * @param imagem    A imagem da questão.
	 * 
	 * 
	 */
	public Questao(String tipo, String enunciado, String fonte, String autor, String espelho,
			List<Alternativa> alternativas) {

		this.tipo = tipo;
		this.enunciado = enunciado;
		this.fonte = fonte;
		this.autor = autor;
		this.espelho = espelho;
		this.alternativas = alternativas;
		this.competencias = new HashSet<CompetenciaType>();
	}

	public Questao() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Recupera o tipo da questão.
	 *
	 * @return O tipo da questão.
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo O tipo da questão.
	 *
	 *             Atualiza o tipo da questão.
	 *
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Recupera o enunciado da questão.
	 *
	 * @return O enunciado da questão.
	 */
	public String getEnunciado() {
		return enunciado;
	}

	/**
	 * @param enunciado O enunciado da questão.
	 *
	 *                  Atualiza o enunciado da questão.
	 *
	 */
	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	/**
	 * Recupera a fonte da questão.
	 *
	 * @return A fonte da questão.
	 */
	public String getFonte() {
		return fonte;
	}

	/**
	 * @param fonte A fonte da questão.
	 *
	 *              Atualiza a fonte da questão.
	 *
	 */
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	/**
	 * Recupera o autor da questão.
	 *
	 * @return O autor da questão.
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * @param autor O autor da questão.
	 *
	 *              Atualiza o autor da questão.
	 *
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getEspelho() {
		return espelho;
	}

	public void setEspelho(String espelho) {
		this.espelho = espelho;
	}

	public List<Alternativa> getAlternativas() {
		return alternativas;
	}

	public void setAlternativas(List<Alternativa> alternativas) {
		this.alternativas = alternativas;
	}

	public Set<CompetenciaType> getCompetencias() {
		return competencias;
	}

	public void setCompetencias(Set<CompetenciaType> competencias) {
		this.competencias = competencias;
	}

}
