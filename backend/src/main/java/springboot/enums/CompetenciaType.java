package springboot.enums;

public enum CompetenciaType {
	COLETA(1),
	PARALELIZAÇÃO(2),
	ANÁLISE(3),
	REPRESENTAÇÃO(4),
	DECOMPOSIÇÃO(5),
	ABSTRAÇÃO(6),
	SIMULAÇÃO(7),
	AUTOMAÇÃO(8),
	ALGORITMOS(9);
	
	
	public final int value;

	CompetenciaType(final int value) {
	    this.value = value;
	 }
	
}
