package springboot.model;

import java.util.List;


import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@ManyToOne
	private Usuario autor;

	private List<Questao> questoes;
	
	@TextScore 
	private Float score;

	public ListaQuestoes(String nomeLista, Usuario autor, List<Questao> questoes) {
		this.nomeLista = nomeLista;
		this.autor = autor;
		this.questoes = questoes;
	}

	public ListaQuestoes() {

	}
	
	
	public String getNomeLista() {
		return nomeLista;
	}

	public void setNomeLista(String nomeLista) {
		this.nomeLista = nomeLista;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public List<Questao> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(List<Questao> questoes) {
		this.questoes = questoes;
	}





}
