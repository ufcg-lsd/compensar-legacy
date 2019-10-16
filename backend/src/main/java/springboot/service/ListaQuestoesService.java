package springboot.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	private final String errorMessage = "Lista de Questões não está cadastrada.";
	public static final int NOME_LISTA = 0;

	
	private ArrayList<String> arrayParametros = new ArrayList<String>();
	private ArrayList<String> arrayOperadores = new ArrayList<String>();
	private ArrayList<String> arrayQuery = new ArrayList<String>();
	private ArrayList<Object> parametros = new ArrayList<Object>();

	@Autowired
	private ListaQuestoesRepository listaQuestoesRepository;


	public ListaQuestoes save(ListaQuestoes listaQuestoes) {
		listaQuestoesRepository.save(listaQuestoes);
		return listaQuestoes;
	}
	
	
	public ListaQuestoes update(ListaQuestoes listaQuestoes, String id) {
		Optional<ListaQuestoes> optListaQuestoes = listaQuestoesRepository.findById(id);

		if (!optListaQuestoes.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		ListaQuestoes novaListaQuestoes = optListaQuestoes.get();

		if (!listaQuestoes.getAutor().equals(novaListaQuestoes.getAutor())) {
			throw new PermissionDeniedException("A lista é de propriedade de outro usuário");
		}

		novaListaQuestoes.setNomeLista(listaQuestoes.getNomeLista());
		novaListaQuestoes.setAutor(listaQuestoes.getAutor());
		novaListaQuestoes.setQuestoes(listaQuestoes.getQuestoes());

		listaQuestoesRepository.save(novaListaQuestoes);

		return novaListaQuestoes;
	}

	public ListaQuestoes delete(String id) {
		Optional<ListaQuestoes> optListaQuestoes = listaQuestoesRepository.findById(id);

		if (!optListaQuestoes.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		ListaQuestoes listaQuestoes = optListaQuestoes.get();
		listaQuestoesRepository.delete(listaQuestoes);

		return listaQuestoes;
	}

	
	private void iniciaColecoes() {
		arrayParametros.add("{$text:{$search:");
		arrayParametros.add("{email:");

		arrayOperadores.add("{'$or':[");
		arrayOperadores.add("{'$and':[");
	}
	
	public Page<ListaQuestoes> getAll(Usuario usuario, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return listaQuestoesRepository.getByAutor(usuario.getEmail(), pageable);
	}
	
	
	public ListaQuestoes getById(String id) {
		Optional<ListaQuestoes> optListaQuestoes = listaQuestoesRepository.findById(id);

		if (!optListaQuestoes.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		return optListaQuestoes.get();
	}

	public Page<ListaQuestoes> getByUser(Usuario user, int page, int size) {


	    Sort sort = Sort.by(
	    	    Sort.Order.desc("score"));
	    
	    Pageable pageable = PageRequest.of(page, size, sort);
	    
	    Page<ListaQuestoes> pagina = listaQuestoesRepository.getByAutor(user.getEmail(),pageable);

	    
		return pagina;
	}

	private boolean isNull(Object parametro) {
		return parametro.equals("null");
	}


}
