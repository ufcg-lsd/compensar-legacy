package springboot.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.Usuario;
import springboot.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private static final String ERROR_MESSAGE = "Usuário ainda não registrado no sistema!";

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Salva um novo usuário no banco de dados.
	 *
	 * @param usuario O usuário a ser salvo.
	 * @return O usuário salvo.
	 */
	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	/**
	 * Deleta um usuário existente pelo e-mail.
	 *
	 * @param email O e-mail do usuário a ser deletado.
	 * @return O usuário deletado.
	 * @throws RegisterNotFoundException Se o usuário não for encontrado.
	 */
	public Usuario delete(String email) {
		Usuario usuario = usuarioRepository.findById(email)
				.orElseThrow(() -> new RegisterNotFoundException(ERROR_MESSAGE));

		usuarioRepository.delete(usuario);
		return usuario;
	}

	/**
	 * Atualiza as informações de um usuário existente.
	 *
	 * @param novoUsuario O objeto com as novas informações do usuário.
	 * @param email       O e-mail do usuário a ser atualizado.
	 * @return O usuário atualizado.
	 * @throws RegisterNotFoundException Se o usuário não for encontrado.
	 */
	public Usuario update(Usuario novoUsuario, String email) {
		usuarioRepository.findById(email).orElseThrow(() -> new RegisterNotFoundException(ERROR_MESSAGE));
		return usuarioRepository.save(novoUsuario);
	}

	/**
	 * Recupera todos os usuários do banco de dados.
	 *
	 * @return A lista de todos os usuários.
	 */
	public List<Usuario> getAll() {
		return usuarioRepository.findAll();
	}

	/**
	 * Recupera um usuário específico pelo e-mail.
	 *
	 * @param email O e-mail do usuário a ser recuperado.
	 * @return O usuário encontrado.
	 * @throws RegisterNotFoundException Se o usuário não for encontrado.
	 */
	public Usuario getById(String email) {
		return usuarioRepository.findById(email)
				.orElseThrow(() -> new RegisterNotFoundException(ERROR_MESSAGE));
	}
}
