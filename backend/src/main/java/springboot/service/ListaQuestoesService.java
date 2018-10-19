package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.exception.RegisterNotFoundException;
import springboot.model.ListaQuestoes;
import springboot.repository.ListaQuestoesRepository;

@Service
public class ListaQuestoesService {
	
	private final String errorMessage = "Lista de Questões não está cadastrada.";

	@Autowired
	private ListaQuestoesRepository listaQuestoesRepository;

	public List<ListaQuestoes> getAll() {
		return listaQuestoesRepository.findAll();
	}

	public ListaQuestoes save(ListaQuestoes listaQuestoes) {
		listaQuestoesRepository.save(listaQuestoes);
		return listaQuestoes;
	}

	public ListaQuestoes getByEmail(String email) {
		Optional<ListaQuestoes> optListaQuestoes = listaQuestoesRepository.pesquisarPorEmail(email);

		if (!optListaQuestoes.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		
		return optListaQuestoes.get();
	}

	public ListaQuestoes update(ListaQuestoes listaQuestoes, String email) {
		Optional<ListaQuestoes> optListaQuestoes = listaQuestoesRepository.pesquisarPorEmail(email);

		if (!optListaQuestoes.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		ListaQuestoes novaListaQuestoes = optListaQuestoes.get();
		novaListaQuestoes.setEmail(listaQuestoes.getEmail());
		novaListaQuestoes.setQuestoes(listaQuestoes.getQuestoes());

		listaQuestoesRepository.save(novaListaQuestoes);

		return novaListaQuestoes;
	}

	public ListaQuestoes delete(String email) {
		Optional<ListaQuestoes> optListaQuestoes = listaQuestoesRepository.pesquisarPorEmail(email);

		if (!optListaQuestoes.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		ListaQuestoes listaQuestoes = optListaQuestoes.get();
		listaQuestoesRepository.delete(listaQuestoes);

		return listaQuestoes;
	}

	public ListaQuestoes deleteByID(Long id) {
		Optional<ListaQuestoes> optListaQuestoes = listaQuestoesRepository.findById(id);

		if (!optListaQuestoes.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		ListaQuestoes listaQuestoes = optListaQuestoes.get();
		listaQuestoesRepository.delete(listaQuestoes);

		return listaQuestoes;
	}

}
