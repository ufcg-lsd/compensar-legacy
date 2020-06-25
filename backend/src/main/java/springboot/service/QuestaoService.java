package springboot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import net.htmlparser.jericho.Source;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;
import springboot.exception.data.NoPendentQuestionException;
import springboot.exception.data.PermissionDeniedException;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.Conteudo;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.repository.QuestaoRepository;
import springboot.util.CustomAggregationOperation;

@Service
public class QuestaoService {

	private final String errorMessage = "A questão não está cadastrada.";
	public static final int ENUNCIADO = 0;
	public static final int COMPETENCIA = 1;
	public static final int ESTADO = 2;
	public static final int CONTEUDO = 7;

	private ArrayList<String> arrayParametros = new ArrayList<String>();
	private ArrayList<String> arrayQuery = new ArrayList<String>();
	private ArrayList<Object> parametros = new ArrayList<Object>();

	@Autowired
	private QuestaoRepository questaoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

	public Questao save(Questao questao) throws IOException {
		
		// Aqui chama o classificador e atualiza o objeto questao
		//questao.setCompetencias(getSetCompetencias(questao.getEnunciado()));

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

	public Questao update(Questao questao, String id) throws IOException {

		Optional<Questao> optQuestao = questaoRepository.findById(id);

		if (!optQuestao.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		Questao novaQuestao = optQuestao.get();

		if (!questao.getAutor().equals(novaQuestao.getAutor())) {
			throw new PermissionDeniedException("A questão é de propriedade de outro usuário");
		}




        questao.setId(id);
		if (novaQuestao.getEstado() != EstadoQuestao.RASCUNHO) {
			questao.setOriginalEnunciado(novaQuestao.getOriginalEnunciado());
		}
		questao.setCompetenciasClassificador(novaQuestao.getCompetenciasClassificador());

		questaoRepository.save(questao);

		return questao;
	}

	public Page<Questao> getAll(int page, int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    Page<Questao> pagina = questaoRepository.findAllByEstado(EstadoQuestao.PUBLICADA, pageable);
	    
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
		arrayParametros.add("{\"enunciado\": { \"$regex\": ");
		arrayParametros.add("{\"competencias\": { \"$in\": ");
		arrayParametros.add("{\"estado\": { \"$in\": ");
		arrayParametros.add("{\"autorInfo._id\": { \"$regex\": ");
		arrayParametros.add("{\"autorInfo.nome\": { \"$regex\": ");
		arrayParametros.add("{\"fonte\": { \"$regex\": ");
		arrayParametros.add("{\"tipo\": { \"$regex\": ");
		arrayParametros.add("{\"conteudo\": { \"$in\": ");



	}

	public Page<Questao> getByEnunciadoCompetenciasAutorFonteTipo(String enunciado, HashSet<String> competencias,
		String autorNome, String autorEmail, String fonte, String tipo, Set<String> conteudo, Set<EstadoQuestao> estados, int page, int size) {

		List<CustomAggregationOperation>  aggList = new ArrayList<>();
		aggList.add(new CustomAggregationOperation(Document.parse(
			"{\n" +
			"    \"$lookup\": {\n" +
			"        \"from\": \"usuario\",\n" +
			"        \"localField\": \"autor\",\n" +
			"        \"foreignField\": \"_id\",\n" +
			"        \"as\": \"autorInfo\"\n" +
			"    }\n" +
			"}"
		)));
		aggList.add(new CustomAggregationOperation(Document.parse("{ \"$project\": { \"autorInfo\": false } }")));
		iniciaColecoes();

		/*
		enunciado = (enunciado.equals("null")) ? "null" : enunciado;
		autorEmail = (autorEmail.equals("null")) ? "" : autorEmail;
		autorNome = (autorNome.equals("null")) ? "" : autorNome;
		fonte = (fonte.equals("null")) ? "" : fonte;
		tipo = (tipo.equals("null")) ? "" : tipo;
		conteudo = (conteudo.equals("null")) ? "" : conteudo;
		*/
		parametros.add(enunciado);

		if (competencias.contains(null))
			parametros.add(new HashSet<>());
		else if (competencias.contains("COMP_TODAS"))
			parametros.add("null");
		else
			parametros.add(competencias);
		if (estados.contains(null))
			parametros.add(new HashSet<>());
		else {
			if (estados.contains(EstadoQuestao.PEND_AVALIACAO))
				estados.add(EstadoQuestao.PEND_APROVACAO);
			parametros.add(estados);
		}
		parametros.add(autorEmail);
		parametros.add(autorNome);
		parametros.add(fonte);
		parametros.add(tipo);
		parametros.add(conteudo);
		
		// inicio da query com o operador lógico AND 

		String query = "{ \"$match\": {\"$and\":[";


		for (int i = 0; i < parametros.size(); i++) {
			if (!isNull(parametros.get(i))) {
				if (i == ENUNCIADO) {
					arrayQuery.add(arrayParametros.get(ENUNCIADO) + " '" + parametros.get(ENUNCIADO) + "'}}");
				} else if (i == COMPETENCIA) {
					String subQuery = arrayParametros.get(COMPETENCIA) + "[";

					for(String competencia : competencias) {
						if (!subQuery.endsWith("[")) {
							subQuery += ", ";
						}
						subQuery += "'" + competencia + "'";
					}
					subQuery += "]}}";
					arrayQuery.add(subQuery);
				} else if (i == ESTADO) {
					String subQuery = arrayParametros.get(ESTADO) + "[";

					for(EstadoQuestao estado : estados) {
						if (!subQuery.endsWith("[")) {
							subQuery += ", ";
						}
						subQuery += "'" + estado + "'";
					}
					subQuery += "]}}";
					arrayQuery.add(subQuery);
				} else if (i == CONTEUDO) {
					if (conteudo.isEmpty()) continue;
					String subQuery = arrayParametros.get(CONTEUDO) + "[";

					for(String conteudoItem : conteudo) {
						if (!subQuery.endsWith("[")) {
							subQuery += ", ";
						}
						subQuery += "'" + conteudoItem + "'";
					}
					subQuery += "]}}";
					arrayQuery.add(subQuery);
				} else {
					// Precisa de uma chave de fechamento e aspas
					arrayQuery.add(arrayParametros.get(i) + "/" + Pattern.quote((String)parametros.get(i)) + "/i}}");
				}
			}
		}

		query += String.join(",", arrayQuery);

		// fechamento do operador lógico AND
		query += "]} }";

		aggList.add(1, new CustomAggregationOperation(Document.parse(query)));

		System.out.println(query);
		parametros.clear();
		arrayQuery.clear();

		Sort sort = Sort.by(Sort.Order.desc("score"));

		Pageable pageable = PageRequest.of(page, size, sort);

		Aggregation agg = Aggregation.newAggregation(aggList);
		List<Questao> results = mongoTemplate.aggregate(agg, "questao", Questao.class).getMappedResults();
		List<Questao> subResults = results.subList(Math.min(page*size, results.size()), Math.min((page+1)*size, results.size()));
		return new PageImpl<Questao>(subResults,pageable, results.size());

	}

	public Set<CompetenciaType> getSetCompetencias(String enunciado) throws IOException  {

		String enunciadoText = extractAllText(enunciado);

		Set<CompetenciaType> competencias = new HashSet<>();

		URL obj = new URL("https://question-classifier.herokuapp.com/classifier/");
		HttpsURLConnection postConnection = (HttpsURLConnection) obj.openConnection();
		postConnection.setRequestMethod("POST");
		postConnection.setRequestProperty("Content-Type", "text/plain");
		postConnection.setDoOutput(true);
		postConnection.setConnectTimeout(10000);
		OutputStream os = postConnection.getOutputStream();
		os.write(enunciadoText.getBytes());
		os.flush();
		os.close();
		int responseCode = postConnection.getResponseCode();


		BufferedReader in = new BufferedReader(new InputStreamReader(
				postConnection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();


		List<String> result = Arrays.asList(response.toString().split(","));


		for (int i = 0; i < result.size(); i++) {
			List<String> compChave = Arrays.asList(result.get(i).split(":"));

			if (compChave.get(1).charAt(2) == '1') {
				competencias.add(getCompetencia(compChave.get(0)));
			}
		}


		return competencias;
	}
	
	private static String extractAllText(String htmlText) {
	    Source source = new Source(htmlText);
	    return source.getTextExtractor().toString();
	}

	private CompetenciaType getCompetencia(String chave) {
		CompetenciaType valor = null;
		for (CompetenciaType competencia : CompetenciaType.values()) {
			if (chave.contains(competencia.value)) {
				valor = competencia;
			}
		}
		return valor;
	}

	private boolean isNull(Object parametro) {
		return parametro.equals("null");
	}

	public Questao getPendente(Usuario usuario) {

        List<CustomAggregationOperation>  aggList = new ArrayList<>();
		aggList.add(new CustomAggregationOperation(Document.parse(
			"{\n" +
			"    \"$lookup\": {\n" +
			"        \"from\": \"avaliacao\",\n" +
			"        let: { id: { \"$toString\": \"$_id\" } },\n" +
			"        \"pipeline\": [{\n" +
			"            \"$match\": {\n" +
			"                \"$expr\": {\n" +
			"                    \"$and\": [\n" +
			"                        { \"$eq\": [ \"$$id\", \"$questao\" ] },\n" +
			"                        { \"$eq\": [ \"$autor\", \"" + usuario.getEmail() + "\" ] }\n" +
			"                    ]\n" +
			"                }\n" +
			"            }\n" +
			"        },\n" +
			"        { $limit: 1 }],\n" +
			"        \"as\": \"avaliacao\"\n" +
			"    }\n" +
			"}"
		)));
		aggList.add(new CustomAggregationOperation(Document.parse("{ \"$match\": { \"avaliacao._id\": { \"$exists\": false }, \"estado\": \"PEND_AVALIACAO\" } }")));
		aggList.add(new CustomAggregationOperation(Document.parse("{ \"$sort\" : { \"ultimoAcesso\" : 1 } }")));
		aggList.add(new CustomAggregationOperation(Document.parse("{ \"$limit\" : 1 }")));
		aggList.add(new CustomAggregationOperation(Document.parse("{ \"$project\": { \"avaliacao\": false } }")));
		Questao melhorQuestao;
		try {
			Aggregation agg = Aggregation.newAggregation(aggList);
			List<Questao> results = mongoTemplate.aggregate(agg, "questao", Questao.class).getMappedResults();
			melhorQuestao = results.get(0);
			melhorQuestao.setUltimoAcesso(System.currentTimeMillis());
			update(melhorQuestao, melhorQuestao.getId());
		} catch(Exception e) {
			throw new NoPendentQuestionException("Não existe nenhuma questão pendente de avaliação");
		}

		return melhorQuestao;
	}
	public Questao getAvaliada() {
		List<Questao> results = questaoRepository.getByEstadoAndQtdAvaliacoesGreaterThan(EstadoQuestao.PEND_APROVACAO, 2);
		if (results.size() == 0) {
			throw new NoPendentQuestionException("Não existe nenhuma questão pendente de aprovação");
		}
		return results.get(0);
	}

	// Busca duas questões (uma com a competência recebida e outra sem a competência recebida) e as embaralha numa lista
	public List<Questao> getSamples(String competencia) {
		List<CustomAggregationOperation>  aggList1 = new ArrayList<>();
		List<CustomAggregationOperation>  aggList2 = new ArrayList<>();
		aggList1.add(new CustomAggregationOperation(Document.parse(
				"{\n" +
				"    $match: {\n" +
				"        competencias: \"" + competencia + "\",\n" +
				"        estado: \"PUBLICADA\"\n" +
				"    }\n" +
				"}"
		)));
		aggList2.add(new CustomAggregationOperation(Document.parse(
				"{\n" +
				"    $match: {\n" +
				"        competencias: { $ne: \"" + competencia + "\" },\n" +
				"        estado: \"PUBLICADA\"\n" +
				"    }\n" +
				"}"
		)));
		aggList1.add(new CustomAggregationOperation(Document.parse("{ $sample: { size: 1 } }")));
		aggList2.add(new CustomAggregationOperation(Document.parse("{ $sample: { size: 1 } }")));
		Aggregation agg = Aggregation.newAggregation(aggList1);
		List<Questao> results = new ArrayList();
		for (Questao q : mongoTemplate.aggregate(agg, "questao", Questao.class).getMappedResults()) {
			results.add(q);
		}
		agg = Aggregation.newAggregation(aggList2);
		for (Questao q : mongoTemplate.aggregate(agg, "questao", Questao.class).getMappedResults()) {
			results.add(q);
		}
		//Embaralha a ordem de retrno das questões de exemplo (com ou sem a competência)
		Collections.swap(results, 0, new Random().nextInt(2));
		return results;
	}

	public Questao getSample(String competencia) {
		return getSamples(competencia).get(new Random().nextInt(2));
	}

	public Boolean hasCompetencia(String competencia, Set<CompetenciaType> competencias) {
		for (CompetenciaType c : competencias) {
			if (c.name().equals(competencia)){
				return true;
			}
		}
		return false;
	}

	public Boolean evaluateQuestao(String competencia, String questao) {
		Questao q = getById(questao);
		return hasCompetencia(competencia, q.getCompetencias());
	}

	// Retorna lista de booleanos que representa se as questoes passadas como parametro possuem ou não a competência buscada
	public List<Boolean> evaluateQuestoes(String competencia, List<String> questoes) {
		List<Boolean> ret = new ArrayList<>();
		for (String id : questoes) {
			ret.add(evaluateQuestao(competencia, id));
		}
		return ret;
	}



	public boolean updateClassificador() {
		List<Questao> l = questaoRepository.findAll();
		for (Questao q : l) {
			try {
				q.setCompetenciasClassificador(getSetCompetencias(q.getEnunciado()));
				questaoRepository.save(q);
			}
			catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
