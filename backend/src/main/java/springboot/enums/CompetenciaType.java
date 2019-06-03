package springboot.enums;

public enum CompetenciaType {
	COMP_COLETA("collect"),
	COMP_PARALELIZAÇÃO("analysis"),
	COMP_ANÁLISE("representation"),
	COMP_REPRESENTAÇÃO("decomposition"),
	COMP_DECOMPOSIÇÃO("algorithms"),
	COMP_ABSTRAÇÃO("abstraction"),
	COMP_SIMULAÇÃO("automation"),
	COMP_AUTOMAÇÃO("parallelization"),
	COMP_ALGORITMOS("simulation");
	
	
	public final String value;

	CompetenciaType(final String value) {
	    this.value = value;
	 }
	
}
