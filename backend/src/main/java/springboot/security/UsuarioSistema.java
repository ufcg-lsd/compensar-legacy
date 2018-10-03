package springboot.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;

	private String nome;

	public UsuarioSistema(String nome, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(email, password, authorities);

		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}