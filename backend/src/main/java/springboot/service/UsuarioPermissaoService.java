package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.exception.data.RegisterNotFoundException;
import springboot.model.UsuarioPermissao;
import springboot.repository.UsuarioPermissaoRepository;

@Service
public class UsuarioPermissaoService {

	private final String errorMessage = "Usuário ainda não registrado no sistema!";

	@Autowired
	private UsuarioPermissaoRepository usuarioPermissaoRepository;

	public UsuarioPermissao savePermissao(UsuarioPermissao usuarioPermissao) {
		usuarioPermissaoRepository.save(usuarioPermissao);
		return usuarioPermissao;
	}
	
	public UsuarioPermissao update(UsuarioPermissao usuarioPermissao, String email) {
		Optional<UsuarioPermissao> optUsuario = usuarioPermissaoRepository.findById(email);

		if (!optUsuario.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		UsuarioPermissao novoUsuarioPermissao = optUsuario.get();
		novoUsuarioPermissao.setEmail(usuarioPermissao.getEmail());
		novoUsuarioPermissao.setPermissao(usuarioPermissao.getPermissao());

		usuarioPermissaoRepository.save(novoUsuarioPermissao);

		return novoUsuarioPermissao;
	}
	
	public List<UsuarioPermissao> getAll() {
		return usuarioPermissaoRepository.findAll();
	}

	public UsuarioPermissao getById(String email) {
		Optional<UsuarioPermissao> optUsuario = usuarioPermissaoRepository.findById(email);

		if (!optUsuario.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		return optUsuario.get();
	}

	
}
