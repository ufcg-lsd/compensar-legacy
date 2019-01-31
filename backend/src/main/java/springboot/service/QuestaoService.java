package springboot.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.exception.RegisterNotFoundException;
import springboot.model.Questao;
import springboot.repository.QuestaoRepository;

@Service
public class QuestaoService {
	
	
	private final String errorMessage = "A questão subjetiva não está cadastrada.";
	public static final int ENUNCIADO = 0;
	public static final int COMPETENCIA = 1;


	
	private ArrayList<String> arrayParametros = new ArrayList<String>();
	private ArrayList<String> arrayOperadores = new ArrayList<String>();
	private ArrayList<String> arrayQuery = new ArrayList<String>();
	private ArrayList<Object> parametros = new ArrayList<Object>();




	@Autowired
	private QuestaoRepository questaoRepository;

	public Questao save(Questao questao) {

		// Aqui chama o classificador e atualiza o objeto questao

		questaoRepository.save(questao);
		return questao;
	}

	public Questao delete(String id) {
		Optional<Questao> optQuestao = questaoRepository.findById(id);

		if (!optQuestao.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		Questao questao = optQuestao.get();

		questaoRepository.delete(questao);
		return questao;
	}

	public Questao update(Questao questao, String id) {

		Optional<Questao> optQuestao = questaoRepository.findById(id);

		if (!optQuestao.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		Questao novaQuestao = optQuestao.get();
		novaQuestao.setFonte(questao.getFonte());
		novaQuestao.setAutor(questao.getAutor());
		novaQuestao.setTipo(questao.getTipo());
		novaQuestao.setEnunciado(questao.getEnunciado());
		novaQuestao.setCompetencias(questao.getCompetencias());
		novaQuestao.setEspelho(questao.getEspelho());
		novaQuestao.setAlternativas(questao.getAlternativas());

		questaoRepository.save(novaQuestao);

		return novaQuestao;
	}

	public List<Questao> getAll() {

		return questaoRepository.findAll();
	}

	public Questao getById(String id) {
		Optional<Questao> optQuestao = questaoRepository.findById(id);

		if (!optQuestao.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		return optQuestao.get();
	}
	
	
	private void iniciaColecoes() {
		arrayParametros.add("{$text: {$search:");
		arrayParametros.add("{competencias:  {$all:");
		arrayParametros.add("{autor:");
		arrayParametros.add("{fonte:");
		arrayParametros.add("{tipo:");
		
		arrayOperadores.add("{'$or':[");
		arrayOperadores.add("{'$and':[");
		arrayOperadores.add("{score: {$meta: \\\"textScore\\\"}}.sort({score:{$meta:\\\"textScore\\\"}})");
	}

	

	public List<Questao> getByEnunciadoCompetenciasAutorFonteTipo(String enunciado, HashSet<String> competencias, String autor,
			String fonte, String tipo) {
					
		iniciaColecoes();
				
		parametros.add(enunciado);
		if (competencias.contains("null")) parametros.add("null");
		else parametros.add(competencias);
		parametros.add(autor);
		parametros.add(fonte);
		parametros.add(tipo);
		
		boolean textIndex = false;
		
		if (!isNull(enunciado)) textIndex = true;
		
		// inicio da query com o operador lógico AND
		String query = arrayOperadores.get(1);
		
		for (int i = 0; i < parametros.size(); i++) {
			if (!isNull(parametros.get(i))) {
					if (i == ENUNCIADO) {
						//Precisa de duas chaves de fechamento e aspas
						arrayQuery.add(arrayParametros.get(i) +" '"+ parametros.get(i) + "'}}");
					} else if (i == COMPETENCIA) {
						//Precisa de duas chaves de fechamento e sem aspas
						arrayQuery.add(arrayParametros.get(i) + parametros.get(i) + "}}");
					} else {
						//Precisa de uma chave de fechamento e aspas
						arrayQuery.add(arrayParametros.get(i) +" '"+ parametros.get(i) + "'}");
					}
			}
		}
		
		query += String.join(",",arrayQuery);
		
		// fechamento do operador lógico AND
		query += "]}";
		
		// adição do rankeamento baseado no score
		if (textIndex) query += "," + arrayOperadores.get(2);
		
		System.out.println(query);
		parametros.clear();
		arrayQuery.clear();
		
		return questaoRepository.getByEnunciadoCompetenciasAutorFonteTipo(query);
	
	
	}	
	
	
	private boolean isNull(Object parametro) {
		return parametro.equals("null");
	}
	

	

	

}
