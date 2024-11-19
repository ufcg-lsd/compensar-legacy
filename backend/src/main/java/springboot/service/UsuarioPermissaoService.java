package springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import springboot.exception.data.PermissionDeniedException;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.UsuarioPermissao;
import springboot.repository.UsuarioPermissaoRepository;

@Service
public class UsuarioPermissaoService {

	private static final String ERROR_MESSAGE = "Usuário ainda não registrado no sistema!";

	private final UsuarioPermissaoRepository usuarioPermissaoRepository;

	public UsuarioPermissaoService(UsuarioPermissaoRepository usuarioPermissaoRepository) {
		this.usuarioPermissaoRepository = usuarioPermissaoRepository;
	}

	public UsuarioPermissao savePermissao(UsuarioPermissao usuarioPermissao) {
		return usuarioPermissaoRepository.save(usuarioPermissao);
	}

	public UsuarioPermissao update(UsuarioPermissao usuarioPermissao, String email) {
		UsuarioPermissao existingUsuarioPermissao = findUsuarioById(email);

		if (!existingUsuarioPermissao.getEmail().equals(email)) {
			throw new PermissionDeniedException("A permissão de um usuário só pode ser alterada por ele mesmo.");
		}

		existingUsuarioPermissao.setPermissao(usuarioPermissao.getPermissao());
		return usuarioPermissaoRepository.save(existingUsuarioPermissao);
	}

	public List<UsuarioPermissao> getAll() {
		return usuarioPermissaoRepository.findAll();
	}

	public UsuarioPermissao getById(String email) {
		return findUsuarioById(email);
	}
	
	private UsuarioPermissao findUsuarioById(String email) {
		return usuarioPermissaoRepository.findById(email)
				.orElseThrow(() -> new RegisterNotFoundException(ERROR_MESSAGE));
	}
}
