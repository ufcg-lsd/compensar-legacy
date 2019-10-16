package springboot.model;

import java.util.List;


import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "listaquestoes")
public class ListaQuestoes{
	
	@Id
	@JsonProperty
	private String id;

	@NotNull
	@TextIndexed
	private String nomeLista;

	private String autor;


	private List<String> questoes;
	
	@TextScore 
	private Float score;

	public ListaQuestoes(String nomeLista, String autor, List<String> questoes) {
		this.nomeLista = nomeLista;
		this.autor = autor;
		this.questoes = questoes;
	}

	public ListaQuestoes() {

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
