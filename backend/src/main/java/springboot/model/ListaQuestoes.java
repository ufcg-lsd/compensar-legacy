package springboot.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

	private String email;

	private List<Questao> questoes;
	
	@TextScore 
	private Float score;

	public ListaQuestoes(String nomeLista, String email, List<Questao> questoes) {
		this.nomeLista = nomeLista;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Questao> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(List<Questao> questoes) {
		this.questoes = questoes;
	}





}
