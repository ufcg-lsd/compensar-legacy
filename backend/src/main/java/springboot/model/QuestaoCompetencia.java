package springboot.model;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import springboot.enums.CompetenciaType;

@Document(collection = "questao-competencia")
@IdClass(QuestaoCompetenciaPK.class)
public class QuestaoCompetencia {

	@Id
    @JsonProperty
	@Indexed
	private String id_questao;

    @Enumerated(EnumType.STRING)
	@Id
	@Indexed
	private CompetenciaType competencia;

	public QuestaoCompetencia(String id_questao, CompetenciaType competencia) {
		this.competencia = competencia;
		this.id_questao = id_questao;
	}

	public QuestaoCompetencia() {

	}

	public String getId() {
		return id_questao;
	}

	public void setId(String id_questao) {
		this.id_questao = id_questao;
	}

	public CompetenciaType getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaType competencia) {
		this.competencia = competencia;
	}

	@Override
	public String toString() {
		return "QuestaoCompetencia [id=" + id_questao + ", competencia=" + competencia + "]";
	}

}
