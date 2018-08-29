package springboot.model;

/**
 * Classe concreta que estende atributos comuns da classe Questão e adiciona espelho da questão.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino 
 */


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "Quest_Subj")
public class QuestaoSubjetiva extends Questao	{
									
	@Column(nullable = false)
	private String espelho;
	
	public QuestaoSubjetiva(Questao tipo, String enunciado, String fonte, String autor, byte[] image, String espelho) {
		super(tipo,enunciado,fonte,autor,image);
		this.espelho = espelho;
	}
	
	public QuestaoSubjetiva() {
		
	}

	public String getEspelho() {
		return espelho;
	}

	public void setEspelho(String espelho) {
		this.espelho = espelho;
	}

	@Override
	public String toString() {
		return "QuestaoSubjetiva [espelho=" + espelho + "]";
	}

}
