package springboot.model;

/**
 * Classe concreta que estende atributos comuns da classe Questão e adiciona coleção de alternativas.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino 
 */

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "questao-obj")
public class QuestaoObjetiva extends Questao {

	@OneToMany(cascade = CascadeType.ALL)
	@Column(nullable = false)
	private List<Alternativa> alternativas;


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
	 * @param alternativas
	 * 			  A coleção de alternativas de uma questão
	 * 
	 */
	public QuestaoObjetiva(Long id,String tipo, String enunciado, String fonte, String autor, byte[] imagem,
			List<Alternativa> alternativas) {
		super(id,tipo, enunciado, fonte, autor, imagem);
		this.alternativas = alternativas;
	}

	public QuestaoObjetiva() {
	}

	/**
	 * Recupera as alternativas da questão.     
	 *
	 * @return As alternativas da questão.     
	 */
	public List<Alternativa> getAlternativas() {
		return alternativas;
	}
	
	/**
	 * @param alternativas
	 *            As alternativas da questão.           
	 *
	 *            Atualiza a coleção de alternativas da questão.        
	 *
	 */
	public void setAlternativas(List<Alternativa> alternativas) {
		this.alternativas = alternativas;
	}

}
