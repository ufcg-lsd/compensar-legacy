package springboot.model;

import java.util.Set;


import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "listaquestoes")
public class ListaQuestoes {

	@NotNull
	@TextIndexed
	private String email;

	@NotNull
	@TextIndexed
	private Set<Questao> questoes;

	public ListaQuestoes(String email, Set<Questao> questoes) {
		this.email = email;
		this.questoes = questoes;
	}

	public ListaQuestoes() {

	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Questao> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(Set<Questao> questoes) {
		this.questoes = questoes;
	}





}
