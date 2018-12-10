package springboot.model;

/**
 * Classe concreta que estende atributos comuns da classe Questão e adiciona espelho da questão.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino 
 */


import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "questao-subj")
public class QuestaoSubjetiva extends Questao {

	@NotNull
	@TextIndexed
	private String espelho;

	/**
	 * Cria uma QuestaoObjetiva com tipo, enunciado, fonte, autor, imagem e alternativas.
	 *
	 * @param tipo
	 *            O tipo da questão.
	 * @param enunciado
	 *            O enunciado da questão.
	 * @param fonte 
	 * 			  A fonte da questão.
	 * @param autor 
	 * 			  O autor da questão.
	 * @param imagem 
	 * 			  A imagem da questão.          
	 * @param espelho
	 * 			  O espelho de uma questão
	 * 
	 */
	public QuestaoSubjetiva( String tipo, String enunciado, String fonte, String autor, byte[] imagem, String espelho) {
		super( tipo, enunciado, fonte, autor, imagem);
		this.espelho = espelho;
	}

	public QuestaoSubjetiva() {

	}

	/**
	 * Recupera o espelho da questão.     
	 *
	 * @return O espelho da questão.     
	 */
	public String getEspelho() {
		return espelho;
	}

	/**
	 * @param espelho
	 *            O espelho da questão.      
	 *
	 *            Atualiza o espelho da questão.        
	 *
	 */
	public void setEspelho(String espelho) {
		this.espelho = espelho;
	}
	/**
	 * Representacao em string da questão subjetiva.
	 *
	 * @return A representacao em string da questão subjetiva.
	 */
	@Override
	public String toString() {
		return "QuestaoSubjetiva [espelho=" + espelho + "]";
	}
	
	

}
