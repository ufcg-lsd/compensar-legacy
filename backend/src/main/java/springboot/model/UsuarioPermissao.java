package springboot.model;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import springboot.enums.PermissaoType;


@Document(collection = "usuario-permissao")
public class UsuarioPermissao {
	
	@NotNull
	@Id
	@Indexed
	private String email;
	
    @Enumerated(EnumType.STRING)
	@NotNull
	@Indexed
	private PermissaoType permissao;
	
	
	public UsuarioPermissao() {
		
	}
	
	public UsuarioPermissao(String email, PermissaoType permissao) {
		this.email = email;
		this.permissao = permissao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PermissaoType getPermissao() {
		return permissao;
	}

	public void setPermissao(PermissaoType permissao) {
		this.permissao = permissao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((permissao == null) ? 0 : permissao.hashCode());
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
		UsuarioPermissao other = (UsuarioPermissao) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (permissao == null) {
			if (other.permissao != null)
				return false;
		} else if (!permissao.equals(other.permissao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioPermissao [email=" + email + ", permissao=" + permissao + "]";
	}
	
	
	
	
}
