package springboot.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import springboot.model.Usuario;
import springboot.repository.UsuarioRepository;
import springboot.exception.RegisterNotFoundException;
import springboot.repository.UsuarioPermissaoRepository;

@Component
public class AepcUserDetailsService implements UserDetailsService {

	private final String errorMessage = "O Usuário não está cadastrado.";

	@Autowired
	private UsuarioRepository usuarioRepository;

	
	@Autowired
	private UsuarioPermissaoRepository usuarioPermissaoRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioRepository.findById(email);

		if (usuario == null) {
			throw new RegisterNotFoundException(errorMessage);
		}

		return new UsuarioSistema(usuario.get().getNome(), usuario.get().getEmail(), usuario.get().getSenha(), authorities(usuario.get().getEmail()));
	}


	public Collection<? extends GrantedAuthority> authorities(String email) {
		Collection<GrantedAuthority> auths = new ArrayList<>();
		
		auths.add(new SimpleGrantedAuthority("ROLE_" + usuarioPermissaoRepository.findById(email).get().getPermissao()));
			

		return auths;
	}

}