package springboot.model;

import java.util.Collections;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Representa uma lista de questões associada a um autor, armazenada no MongoDB.
 */
@Document(collection = "listaquestoes")
public class ListaQuestoes {

	@Id
	@JsonProperty
	private String id;

	@NotNull
	@TextIndexed
	@NotBlank(message = "O nome da lista não pode estar em branco")
	private String nomeLista;

	@NotNull
	@NotBlank(message = "O autor da lista não pode estar em branco")
	private String autor;

	private List<String> questoes;

	@TextScore
	private Float score;

	/**
	 * Construtor para criar uma lista de questões com nome, autor e questões
	 * associadas.
	 * 
	 * @param nomeLista O nome da lista de questões.
	 * @param autor     O autor que criou a lista de questões.
	 * @param questoes  Uma lista de IDs das questões.
	 */
	public ListaQuestoes(String nomeLista, String autor, List<String> questoes) {
		this.nomeLista = nomeLista;
		this.autor = autor;
		this.questoes = questoes;
	}

	public ListaQuestoes() {
		this.questoes = Collections.emptyList();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNomeLista() {
		return nomeLista;
	}

	public void setNomeLista(String nomeLista) {
		this.nomeLista = nomeLista;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public List<String> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(List<String> questoes) {
		this.questoes = questoes;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}
}
