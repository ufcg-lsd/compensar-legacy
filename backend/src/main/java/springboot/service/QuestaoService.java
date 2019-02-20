package springboot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import springboot.enums.CompetenciaType;
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
		questao.getEnunciadoCompetencia().setCompetencias(getSetCompetencias());

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
		novaQuestao.setEnunciadoCompetencia(questao.getEnunciadoCompetencia());
		novaQuestao.setEspelho(questao.getEspelho());
		novaQuestao.setAlternativas(questao.getAlternativas());

		questaoRepository.save(novaQuestao);

		return novaQuestao;
	}

	public Page<Questao> getAll(int page, int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    Page<Questao> pagina = questaoRepository.findAll(pageable);
	    
		return pagina;
	}

	public Questao getById(String id) {
		Optional<Questao> optQuestao = questaoRepository.findById(id);

		if (!optQuestao.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		return optQuestao.get();
	}

	private void iniciaColecoes() {
		arrayParametros.add("{$text:{$search:");
		arrayParametros.add("{competencias:  {$all:");
		arrayParametros.add("{autor:");
		arrayParametros.add("{fonte:");
		arrayParametros.add("{tipo:");
		arrayParametros.add("{conteudo:");


		arrayOperadores.add("{'$or':[");
		arrayOperadores.add("{'$and':[");
		arrayOperadores.add("{score: {$meta: \\\"textScore\\\"}}.sort({score:{$meta:\\\"textScore\\\"}})");
	}

	public Page<Questao> getByEnunciadoCompetenciasAutorFonteTipo(String enunciado, HashSet<String> competencias,
			String autor, String fonte, String tipo, String conteudo,int page, int size) {

		iniciaColecoes();

		parametros.add(enunciado);
		if (competencias.contains("null"))
			parametros.add("null");
		else
			parametros.add(competencias);
		parametros.add(autor);
		parametros.add(fonte);
		parametros.add(tipo);
		parametros.add(conteudo);

		boolean textIndex = false;

		System.out.println(parametros.size());
		System.out.println(parametros.toString());
		
		
		// inicio da query com o operador lógico AND 

		String query = arrayOperadores.get(1);


		for (int i = 0; i < parametros.size(); i++) {
			if (!isNull(parametros.get(i))) {
				if (i == ENUNCIADO) {
					// Caso tenha enunciado e NÃO competência
					if (isNull(parametros.get(i + 1))) {
						// Precisa de duas chaves de fechamento e aspas
						arrayQuery.add(arrayParametros.get(i) + " '" + parametros.get(i) + "'}}");
					} 
					textIndex = true;
				} else if (i == COMPETENCIA) {
					String subQuery = "";
					// Caso tenha competência E enunciado
					if (!isNull(parametros.get(i - 1))) {
												
						subQuery = arrayParametros.get(i - 1) + " '" + parametros.get(i - 1);
						
						for (String competencia : competencias) {
							subQuery += " " + competencia;
						}
					// Caso tenha competência e NÃO enunciado
					} else {
						subQuery = arrayParametros.get(i - 1) + " '";
						
						for (String competencia : competencias) {
							subQuery += " " + competencia;
						}	
					}
					
					subQuery += "'}}";	
					textIndex = true;

					arrayQuery.add(subQuery);
				} else {
					// Precisa de uma chave de fechamento e aspas
					arrayQuery.add(arrayParametros.get(i) + " '" + parametros.get(i) + "'}");
				}
			}
		}

		query += String.join(",", arrayQuery);

		// fechamento do operador lógico AND

		query += "]}";
	

		// adição do rankeamento baseado no score
		//if (textIndex) query += "," + arrayOperadores.get(2);

		System.out.println(query);
		parametros.clear();
		arrayQuery.clear();
		
	    Sort sort = Sort.by(
	    	    Sort.Order.desc("score"));
	    
	    Pageable pageable = PageRequest.of(page, size, sort);
	    
	    Page<Questao> pagina = questaoRepository.getByEnunciadoCompetenciasAutorFonteTipo(query,pageable);

	    
		return pagina;

	}

	private Set<CompetenciaType> getSetCompetencias() {

		Set<CompetenciaType> competencias = new HashSet<>();
		Set<CompetenciaType> competenciasAleatorias = new HashSet<>();

		ArrayList<Integer> nums = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) {
			nums.add(new Integer(i));
		}

		Collections.shuffle(nums);
		for (int i = 0; i < 9; i++) {
			competencias.add(getCompetencia(nums.get(i)));
		}

		competenciasAleatorias.clear();

		competenciasAleatorias.addAll(competencias);

		competencias.clear();

		return competenciasAleatorias;
	}

	private CompetenciaType getCompetencia(int chave) {
		CompetenciaType valor = null;
		for (CompetenciaType competencia : CompetenciaType.values()) {
			if (competencia.value == chave) {
				valor = competencia;
			}
		}
		return valor;
	}

	private boolean isNull(Object parametro) {
		return parametro.equals("null");
	}

}
