package springboot.model;

import java.util.Collection;
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
// import org.springframework.data.mongodb.core.mapping.TextScore;

import com.fasterxml.jackson.annotation.JsonProperty;

import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;

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

	private String originalEnunciado;

	@TextIndexed
	private Set<CompetenciaType> competencias;

	private Set<CompetenciaType> competenciasClassificador;

	private String fonte;

	private String autor;

	private String espelho;
	
	private HashSet<String> conteudo;

	private List<Alternativa> alternativas;

	private Integer qtdAvaliacoes;

	private Long ultimoAcesso;

	private EstadoQuestao estado;
	
	// @TextScore 
	// private Float score;

	/**
	 * Cria uma Questão com tipo, enunciado, fonte, autor e imagem.
	 *
	 * @param tipo      O tipo da questão.
	 * @param enunciado O enunciado da questão.
	 * @param fonte     A fonte da questão.
	 * @param autor     O autor da questão.
	 * @param espelho    A imagem da questão.
	 * 
	 * 
	 */
	public Questao(String tipo, HashSet<String> conteudo, String enunciado, String fonte, String autor, String espelho,
			List<Alternativa> alternativas, Collection<CompetenciaType> competencias) {

		this.tipo = tipo;
		this.conteudo = conteudo;
		this.enunciado = enunciado;
		this.originalEnunciado = enunciado;
		this.fonte = fonte;
		this.autor = autor;
		this.espelho = espelho;
		this.alternativas = alternativas;
		this.competencias = new HashSet<CompetenciaType>(competencias);
		this.competenciasClassificador = new HashSet<CompetenciaType>(competencias);
		this.qtdAvaliacoes = 0;
		this.ultimoAcesso = System.currentTimeMillis();
		this.estado = EstadoQuestao.RASCUNHO;
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
	
	

	public HashSet<String> getConteudo() {
		return conteudo;
	}

	public void setConteudo(HashSet<String> conteudo) {
		this.conteudo = conteudo;
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

	public String getEnunciado() {
		return enunciado;
	}


	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public String getOriginalEnunciado() {
		return originalEnunciado;
	}

	public void setOriginalEnunciado(String originalEnunciado) {
		this.originalEnunciado = originalEnunciado;
	}

	public Set<CompetenciaType> getCompetencias() {
		return competencias;
	}


	public void setCompetencias(Set<CompetenciaType> competencias) {
		this.competencias = competencias;
	}

	public Set<CompetenciaType> getCompetenciasClassificador() {
		return competenciasClassificador;
	}

	public void setCompetenciasClassificador(Set<CompetenciaType> competenciasClassificador) {
		this.competenciasClassificador = competenciasClassificador;
	}

	// public Float getScore() {
	// 	return score;
	// }

	// public void setScore(Float score) {
	// 	this.score = score;
	// }


	public Integer getQtdAvaliacoes() {
		return qtdAvaliacoes;
	}

	public void setQtdAvaliacoes(Integer qtdAvaliacoes) {
		this.qtdAvaliacoes = qtdAvaliacoes;
	}

	public Long getUltimoAcesso() {
		return ultimoAcesso;
	}

	public void setUltimoAcesso(Long ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}

	public EstadoQuestao getEstado() {
		return estado;
	}

	public void setEstado(EstadoQuestao estado) {
		this.estado = estado;
	}
}
