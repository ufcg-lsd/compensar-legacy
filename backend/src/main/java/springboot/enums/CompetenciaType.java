package springboot.enums;

public enum CompetenciaType {
	COMP_COLETA("collect"),
	COMP_PARALELIZAÇÃO("parallelization"),
	COMP_ANÁLISE("analysis"),
	COMP_REPRESENTAÇÃO("representation"),
	COMP_DECOMPOSIÇÃO("decomposition"),
	COMP_ABSTRAÇÃO("abstraction"),
	COMP_SIMULAÇÃO("simulation"),
	COMP_AUTOMAÇÃO("automation"),
	COMP_ALGORITMOS("algorithms");
	
	
	public final String value;

	CompetenciaType(final String value) {
	    this.value = value;
	 }
	
}
