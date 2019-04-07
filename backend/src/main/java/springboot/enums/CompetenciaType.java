package springboot.enums;

public enum CompetenciaType {
	COMP_COLETA(1),
	COMP_PARALELIZAÇÃO(2),
	COMP_ANÁLISE(3),
	COMP_REPRESENTAÇÃO(4),
	COMP_DECOMPOSIÇÃO(5),
	COMP_ABSTRAÇÃO(6),
	COMP_SIMULAÇÃO(7),
	COMP_AUTOMAÇÃO(8),
	COMP_ALGORITMOS(9);
	
	
	public final int value;

	CompetenciaType(final int value) {
	    this.value = value;
	 }
	
}
