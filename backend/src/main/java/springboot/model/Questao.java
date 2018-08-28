package springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Questao {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;
    
    @Column(nullable = false)
    private Questao tipo;
    
    @Column(nullable = false)
    private String enunciado;
    
    @Column(nullable = true)
    private String fonte;
    
    @Column(nullable = true)
    private String autor;  // depois setar o tipo para Usuario
    
    @Lob
    @Column(nullable=true, columnDefinition="mediumblob")
    private byte[] image;
    
    
    public Questao(Questao tipo, String enunciado, String fonte, String autor, byte[] image) {
    	this.tipo = tipo;
    	this.enunciado = enunciado;
    	this.fonte = fonte;
    	this.autor = autor;
    	this.image = image;
    }
    
    public Questao() {
    	
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Questao getTipo() {
		return tipo;
	}

	public void setTipo(Questao tipo) {
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

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
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
