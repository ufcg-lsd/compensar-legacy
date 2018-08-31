package springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Alternativa")
public class Alternativa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)	
	private Long id_alternativa;
	
	/*
	@ManyToOne // revisar esse relacionamento
	@JoinColumn(name = "id", referencedColumnName = "id")
	private Long id_questao; // revisar esse tratamento de chave estrangeira
	*/
	
	@Column(nullable = false)
	private String texto;
	
	@Column(nullable = false)
	private boolean correta;
	
	public Alternativa(String texto, boolean correta) {
		this.texto = texto;
		this.correta = correta;
	}

	public Long getId_alternativa() {
		return id_alternativa;
	}

	public void setId_alternativa(Long id_alternativa) {
		this.id_alternativa = id_alternativa;
	}


	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public boolean isCorreta() {
		return correta;
	}

	public void setCorreta(boolean correta) {
		this.correta = correta;
	}

	@Override
	public String toString() {
		return "Alternativa [texto=" + texto + ", correta=" + correta + "]";
	}

	
}
