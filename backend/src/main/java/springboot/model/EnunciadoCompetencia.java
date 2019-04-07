package springboot.model;

import java.util.HashSet;
import java.util.Set;

import springboot.enums.CompetenciaType;

public class EnunciadoCompetencia {
	
	private String enunciado;

	private Set<CompetenciaType> competencias;
	
	
	public EnunciadoCompetencia(String enunciado) {
		this.enunciado = enunciado;
		this.competencias = new HashSet<CompetenciaType>();
	}


	public String getEnunciado() {
		return enunciado;
	}


	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}


	public Set<CompetenciaType> getCompetencias() {
		return competencias;
	}


	public void setCompetencias(Set<CompetenciaType> competencias) {
		this.competencias = competencias;
	}
	
}
