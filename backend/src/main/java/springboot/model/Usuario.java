package springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;


@Entity(name = "Usuario")
public class Usuario {
	
	@Column(nullable = false)
	private String nomeCompleto;
	
	@Column(nullable = false)
	private String nomeInstituicao;
	
	@Id
	@Column(nullable = false)
	private String email;
	
	
	public Usuario(String nomeCompleto, String nomeInstituicao,String email) {
		this.nomeCompleto = nomeCompleto;
		this.nomeInstituicao = nomeInstituicao;
		this.email = email;
	}
	
	public Usuario() {
		
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getNomeInstituicao() {
		return nomeInstituicao;
	}

	public void setNomeInstituicao(String nomeInstituicao) {
		this.nomeInstituicao = nomeInstituicao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [nomeCompleto=" + nomeCompleto + ", nomeInstituicao=" + nomeInstituicao + ", email=" + email
				+ "]";
	}

}
