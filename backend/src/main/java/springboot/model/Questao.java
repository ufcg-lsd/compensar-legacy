package springboot.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;

/**
 * Classe que representa uma Questão que pode ser do tipo Objetiva ou Subjetiva.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * Autor: Marcelo Gabriel dos Santos Queiroz Vitorino
 */
@Document(collection = "questao")
public class Questao {

	@Id
	@JsonProperty
	private String id;

	@NotBlank(message = "O tipo da questão não pode ser vazio")
	private String tipo;

	@NotBlank(message = "O enunciado da questão não pode ser vazio")
	@TextIndexed
	private String enunciado;

	@TextIndexed
	private Set<CompetenciaType> competencias = new HashSet<>();

	private Set<CompetenciaType> competenciasClassificador = new HashSet<>();

	private String originalEnunciado;
	private String fonte;
	private String autor;
	private String espelho;

	private Set<String> conteudo = new HashSet<>();
	private List<Alternativa> alternativas;
	private Integer qtdAvaliacoes = 0;
	private Long ultimoAcesso = System.currentTimeMillis();
	private EstadoQuestao estado = EstadoQuestao.RASCUNHO;

	@TextScore
	private Float score;

	/**
	 * Construtor com parâmetros para criação de uma nova Questão.
	 *
	 * @param tipo         O tipo da questão.
	 * @param conteudo     O conteúdo da questão.
	 * @param enunciado    O enunciado da questão.
	 * @param fonte        A fonte da questão.
	 * @param autor        O autor da questão.
	 * @param espelho      A imagem espelho da questão.
	 * @param alternativas As alternativas da questão.
	 * @param competencias As competências relacionadas à questão.
	 */
	public Questao(String tipo, Set<String> conteudo, String enunciado, String fonte, String autor, String espelho,
			List<Alternativa> alternativas, Collection<CompetenciaType> competencias) {
		this.tipo = tipo;
		this.conteudo = conteudo != null ? conteudo : new HashSet<>();
		this.enunciado = enunciado;
		this.originalEnunciado = enunciado;
		this.fonte = fonte;
		this.autor = autor;
		this.espelho = espelho;
		this.alternativas = alternativas;
		if (competencias != null) {
			this.competencias = new HashSet<>(competencias);
			this.competenciasClassificador = new HashSet<>(competencias);
		}
	}

	public Questao() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Set<String> getConteudo() {
		return conteudo;
	}

	public void setConteudo(Set<String> conteudo) {
		this.conteudo = conteudo;
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

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getAutor() {
		return autor;
	}

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

	public Set<CompetenciaType> getCompetenciasClassificador() {
		return competenciasClassificador;
	}

	public void setCompetenciasClassificador(Set<CompetenciaType> competenciasClassificador) {
		this.competenciasClassificador = competenciasClassificador;
	}

	public Float getScore() {
		return score;
	}

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
