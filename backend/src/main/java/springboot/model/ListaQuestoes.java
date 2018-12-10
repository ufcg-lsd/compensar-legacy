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

    @Id
    private ObjectId id;

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

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListaQuestoes other = (ListaQuestoes) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ListaQuestoes [id=" + id + ", email=" + email + ", questoes=" + questoes + "]";
	}

}
