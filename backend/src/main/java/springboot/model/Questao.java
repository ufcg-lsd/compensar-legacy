package springboot.model;

/**
 * Classe abstrata que representa um molde de uma Questão, que pode ser Objetiva ou Subjetiva.
 * 
 * Ambiente de Estudo ao Pensamento Computacional
 * 
 * @author Marcelo Gabriel dos Santos Queiroz Vitorino 
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.mongodb.core.mapping.Document;


@Inheritance(strategy = InheritanceType.JOINED)
@Document(collection = "questao")
public class Questao {

	@Id
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private String tipo;

	@Column(nullable = false)
	private String enunciado;

	@Column(nullable = true)
	private String fonte;

	@Column(nullable = true)
	private String autor;

	@Lob
	@Column(nullable = true, columnDefinition = "mediumblob")
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
	public Questao(Long id,String tipo, String enunciado, String fonte, String autor, byte[] imagem) {
		this.id = id;
		this.tipo = tipo;
		this.enunciado = enunciado;
		this.fonte = fonte;
		this.autor = autor;
		this.imagem = imagem;
	}

	public Questao() {

	}


	/**
	 * Recupera o id da questão.     
	 *
	 * @return O id da questão.     
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id
	 *            O id da questão.     
	 *
	 *            Atualiza o id da questão.     
	 *
	 */
	public void setId(Long id) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Questao other = (Questao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}





}
