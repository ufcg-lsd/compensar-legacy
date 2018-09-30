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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Questao")
public abstract class Questao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_questao", updatable = false, nullable = false)
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

	public Questao(String tipo, String enunciado, String fonte, String autor, byte[] imagem) {
		this.tipo = tipo;
		this.enunciado = enunciado;
		this.fonte = fonte;
		this.autor = autor;
		this.imagem = imagem;
	}

	public Questao() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImage(byte[] imagem) {
		this.imagem = imagem;
	}

	/**
	 * 
	 * Métodos referentes as competências de uma questão.
	 * 
	 */

	/**
	 * public List<Competencia> getCompetencias() { return competencias; }
	 * 
	 * public void setCompetencias(List<Competencia> competencias) {
	 * this.competencias = competencias; }
	 * 
	 * public void addCompetencias(Competencia competencia) {
	 * competencias.add(competencia); }
	 * 
	 * public boolean containsCompetencia(Competencia competencia) { return
	 * competencias.contains(competencia); }
	 * 
	 * public void removeCompetencia(Competencia competencia) {
	 * competencias.remove(competencia); }
	 * 
	 * public boolean semCompetencia() { return competencias.isEmpty(); }
	 * 
	 * public Object[] toArray() { return competencias.toArray(); }
	 * 
	 */

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
