package springboot.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.model.Questao;
import springboot.service.QuestaoService;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "QuestaoControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestaoController {

	@Autowired
	QuestaoService questaoService;

	@ApiOperation("Permite registrar uma nova questão no sistema. Requer que o corpo do request contenha um objeto com os campos: tipo, enunciado, fonte, autor, imagem, espelho ou alternativas.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao", method = RequestMethod.POST)
	public Questao save(@RequestBody Questao questao) {
		return questaoService.save(questao);
	}

	@ApiOperation("Permite apagar uma questão do sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Questao> delete(@PathVariable("id") String id) {
		Questao questao = questaoService.delete(id);
		return new ResponseEntity<Questao>(questao, HttpStatus.OK);
	}

	@ApiOperation("Permite atualizar uma questão do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma questão subjetiva.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Questao> update(@PathVariable("id") String id, @RequestBody Questao questao) {
		Questao updatedQuestao = questaoService.update(questao, id);
		return new ResponseEntity<Questao>(updatedQuestao, HttpStatus.OK);
	}

	@ApiOperation("Fornece um array de objetos do tipo questão registrados.\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao", method = RequestMethod.GET)
	public List<Questao> getAll() {
		return questaoService.getAll();
	}

	@ApiOperation("Fornece os dados de uma questão em particular. O objeto contém: tipo, enunciado, fonte, autor, imagem, competencias e espelho.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.GET)
	public Questao getById(@PathVariable("id") String id) {
		return questaoService.getById(id);
	}

	@ApiOperation("Fornece um array de questões que fazem o match com o enunciado\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/search/{enunciado}", method = RequestMethod.GET)
	public List<Questao> getByEnunciado(@PathVariable("enunciado") String enunciado) {
		return questaoService.getByEnunciado(enunciado);
	}

	@ApiOperation("Fornece um array de questões que fazem o match com o enunciado e com competências\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/search/{enunciado}/{competencias}", method = RequestMethod.GET)
	public List<Questao> getByEnunciadoCompetencias(@PathVariable("enunciado") String enunciado,
			@PathVariable("competencias") HashSet<String> competencias) {
		return questaoService.getByEnunciadoCompetencias(enunciado, competencias);
	}

}
