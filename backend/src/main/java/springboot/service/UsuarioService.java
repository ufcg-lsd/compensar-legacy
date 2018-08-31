package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.model.Usuario;
import springboot.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario save(Usuario usuario) {
		usuarioRepository.save(usuario);
		return usuario;
	}

	public Usuario delete(String email) {
		Optional<Usuario> optUsuario = usuarioRepository.findById(email);

		/*
		if (!optUsuario.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/

		Usuario usuario = optUsuario.get();
		usuarioRepository.delete(usuario);

		return usuario;
	}

	public Usuario update(Usuario usuario, String email) {
		Optional<Usuario> optUsuario = usuarioRepository.findById(email);
		
		/*
		if (!optUsuario.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/

		Usuario novoUsuario = optUsuario.get();
		novoUsuario.setEmail(usuario.getEmail());
		novoUsuario.setNomeCompleto(usuario.getNomeCompleto());
		novoUsuario.setNomeInstituicao(usuario.getNomeInstituicao());

		usuarioRepository.save(novoUsuario);

		return novoUsuario;
	}

	public List<Usuario> getAll() {
		return usuarioRepository.findAll();
	}

	/*
	public Usuario pesquisarPorNome(String nome) {
		return usuarioRepository.pesquisarPorNome(nome);
	}
	*/

}
