package springboot.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import springboot.enums.PermissaoType;

import java.util.Objects;

@Document(collection = "usuario-permissao")
public class UsuarioPermissao {

	@Id
	@Email(message = "E-mail deve ser válido")
	@NotBlank(message = "E-mail não pode ser vazio")
	private String email;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Permissão não pode ser nula")
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
		return Objects.hash(email, permissao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		UsuarioPermissao other = (UsuarioPermissao) obj;
		return Objects.equals(email, other.email) && permissao == other.permissao;
	}

	@Override
	public String toString() {
		return "UsuarioPermissao [email=".concat(email).concat(", permissao=").concat(permissao.toString()).concat("]");
	}
}
