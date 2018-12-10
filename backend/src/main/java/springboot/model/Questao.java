package springboot.model;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Classe abstrata que representa um molde de uma Questão, que pode ser Objetiva ou Subjetiva.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino 
 */



import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "questao")
public class Questao {

    @Id
	@TextIndexed 
    private ObjectId id;

	@NotNull
	@TextIndexed 
	private String tipo;

	@NotNull
	@TextIndexed
	private String enunciado;

	@TextIndexed
	private String fonte;

	@TextIndexed
	private String autor;

	@TextIndexed
	@Lob
	private byte[] imagem;
	

	/**
	 * Cria uma Questão com tipo, enunciado, fonte, autor e imagem.
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
	 *         
	 * 
	 */
	public Questao(String tipo, String enunciado, String fonte, String autor, byte[] imagem) {

		this.tipo = tipo;
		this.enunciado = enunciado;
		this.fonte = fonte;
		this.autor = autor;
		this.imagem = imagem;
	}

	public Questao() {

	}


	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * Recupera o tipo da questão.     
	 *
	 * @return O tipo da questão.     
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            O tipo da questão.        
	 *
	 *            Atualiza o tipo da questão.        
	 *
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Recupera o enunciado da questão.     
	 *
	 * @return O enunciado da questão.     
	 */
	public String getEnunciado() {
		return enunciado;
	}

	/**
	 * @param enunciado
	 *            O enunciado da questão.        
	 *
	 *            Atualiza o enunciado da questão.        
	 *
	 */
	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	/**
	 * Recupera a fonte da questão.     
	 *
	 * @return A fonte da questão.     
	 */
	public String getFonte() {
		return fonte;
	}

	/**
	 * @param fonte
	 *            A fonte da questão.        
	 *
	 *            Atualiza a fonte da questão.        
	 *
	 */
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	/**
	 * Recupera o autor da questão.     
	 *
	 * @return O autor da questão.     
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * @param autor
	 *            O autor da questão.        
	 *
	 *            Atualiza o autor da questão.        
	 *
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	/**
	 * Recupera a imagem da questão.     
	 *
	 * @return A imagem da questão.     
	 */
	public byte[] getImagem() {
		return imagem;
	}

	/**
	 * @param imagem
	 *            A imagem da questão.        
	 *
	 *            Atualiza a imagem da questão.        
	 *
	 */
	public void setImage(byte[] imagem) {
		this.imagem = imagem;
	}





}
