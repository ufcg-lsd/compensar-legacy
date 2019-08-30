package springboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import springboot.exception.data.RegisterNotFoundException;
import springboot.model.ListaQuestoes;
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
		novaListaQuestoes.setNomeLista(listaQuestoes.getNomeLista());
		novaListaQuestoes.setEmail(listaQuestoes.getEmail());
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
	
	public List<ListaQuestoes> getAll() {
		return listaQuestoesRepository.findAll();
	}
	
	
	public ListaQuestoes getById(String id) {
		Optional<ListaQuestoes> optListaQuestoes = listaQuestoesRepository.findById(id);

		if (!optListaQuestoes.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		return optListaQuestoes.get();
	}

	public Page<ListaQuestoes> getByNomeEmail(String nomeLista, String email, int page, int size) {
		
		iniciaColecoes();
		
		parametros.add(nomeLista);
		parametros.add(email);

		// inicio da query com o operador lógico AND 
		String query = arrayOperadores.get(1);

		for (int i = 0; i < parametros.size(); i++) {
			if (!isNull(parametros.get(i))) {
				if (i == NOME_LISTA) {
					// Precisa de duas chaves de fechamento e aspas
					arrayQuery.add(arrayParametros.get(i) + " '" + parametros.get(i) + "'}}");
				} else {
					// Precisa de uma chave de fechamento e aspas
					arrayQuery.add(arrayParametros.get(i) + " '" + parametros.get(i) + "'}");
				}
			}	
		}
		
		query += String.join(",", arrayQuery);

		// fechamento do operador lógico AND
		query += "]}";

		System.out.println(query);
		parametros.clear();
		arrayQuery.clear();
		
	    Sort sort = Sort.by(
	    	    Sort.Order.desc("score"));
	    
	    Pageable pageable = PageRequest.of(page, size, sort);
	    
	    Page<ListaQuestoes> pagina = listaQuestoesRepository.getByNomeEmail(query,pageable);

	    
		return pagina;
	}

	private boolean isNull(Object parametro) {
		return parametro.equals("null");
	}


}
