package springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import springboot.exception.data.PermissionDeniedException;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.ListaQuestoes;
import springboot.model.Usuario;
import springboot.repository.ListaQuestoesRepository;

@Service
public class ListaQuestoesService {

	private static final String ERROR_MESSAGE = "Lista de Questões não está cadastrada.";

	private final ListaQuestoesRepository listaQuestoesRepository;

	public ListaQuestoesService(ListaQuestoesRepository listaQuestoesRepository) {
		this.listaQuestoesRepository = listaQuestoesRepository;
	}

	public ListaQuestoes save(ListaQuestoes listaQuestoes) {
		return listaQuestoesRepository.save(listaQuestoes);
	}

	public ListaQuestoes update(ListaQuestoes listaQuestoes, String id) {
		ListaQuestoes existingListaQuestoes = findListaQuestoesById(id);

		if (!listaQuestoes.getAutor().equals(existingListaQuestoes.getAutor())) {
			throw new PermissionDeniedException("A lista é de propriedade de outro usuário");
		}

		existingListaQuestoes.setNomeLista(listaQuestoes.getNomeLista());
		existingListaQuestoes.setAutor(listaQuestoes.getAutor());
		existingListaQuestoes.setQuestoes(listaQuestoes.getQuestoes());

		return listaQuestoesRepository.save(existingListaQuestoes);
	}

	public ListaQuestoes delete(String id) {
		ListaQuestoes listaQuestoes = findListaQuestoesById(id);
		listaQuestoesRepository.delete(listaQuestoes);
		return listaQuestoes;
	}

	public Page<ListaQuestoes> getAll(Usuario usuario, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return listaQuestoesRepository.getByAutor(usuario.getEmail(), pageable);
	}

	public ListaQuestoes getById(String id) {
		return findListaQuestoesById(id);
	}

	public Page<ListaQuestoes> getByUser(Usuario user, int page, int size) {
		Sort sort = Sort.by(
				Sort.Order.desc("score"));

		Pageable pageable = PageRequest.of(page, size, sort);
		return listaQuestoesRepository.getByAutor(user.getEmail(), pageable);
	}

	private ListaQuestoes findListaQuestoesById(String id) {
		return listaQuestoesRepository.findById(id)
				.orElseThrow(() -> new RegisterNotFoundException(ERROR_MESSAGE));
	}
}
