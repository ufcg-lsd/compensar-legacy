package springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "questao-competencia")
@IdClass(QuestaoCompetenciaPK.class)
public class QuestaoCompetencia {

	@Id
	@Indexed
	private Long id_questao;

	@Id
	@Indexed
	private String competencia;

	public QuestaoCompetencia(Long id_questao, String competencia) {
		this.competencia = competencia;
		this.id_questao = id_questao;
	}

	public QuestaoCompetencia() {

	}

	public Long getId() {
		return id_questao;
	}

	public void setId(Long id_questao) {
		this.id_questao = id_questao;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	@Override
	public String toString() {
		return "QuestaoCompetencia [id=" + id_questao + ", competencia=" + competencia + "]";
	}

}
