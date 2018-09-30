package springboot.model;

import java.io.Serializable;

public class QuestaoCompetenciaPK implements Serializable {
	private Long id_questao;
	private String competencia;

	public QuestaoCompetenciaPK(Long id_questao, String competencia) {
		this.id_questao = id_questao;
		this.competencia = competencia;
	}

	public QuestaoCompetenciaPK() {

	}

	public Long getId_questao() {
		return id_questao;
	}

	public void setId_questao(Long id_questao) {
		this.id_questao = id_questao;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

}
