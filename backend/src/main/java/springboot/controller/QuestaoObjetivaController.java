package springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import springboot.model.QuestaoObjetiva;
import springboot.service.QuestaoObjetivaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "QuestaoObjControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestaoObjetivaController {

	@Autowired
	QuestaoObjetivaService questaoObjService;

	@ApiOperation("Permite registrar uma nova questão objetiva no sistema. Requer que o corpo do request contenha um objeto com os campos: tipo, enunciado, fonte, autor, imagem e alternativas.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoObjetiva.class) })
	@RequestMapping(value = "/questaoObj", method = RequestMethod.POST)
	public QuestaoObjetiva save(@RequestBody QuestaoObjetiva questaoObj) {
		return questaoObjService.save(questaoObj);
	}

	@ApiOperation("Permite apagar uma questão objetiva do sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoObjetiva.class) })
	@RequestMapping(value = "/questaoObj/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<QuestaoObjetiva> delete(@PathVariable("id") String id) {
		QuestaoObjetiva questaoObj = questaoObjService.delete(id);
		return new ResponseEntity<QuestaoObjetiva>(questaoObj, HttpStatus.OK);
	}

	@ApiOperation("Permite atualizar uma questão objetiva do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma questão objetiva .\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoObjetiva.class) })
	@RequestMapping(value = "/questaoObj/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoObjetiva> update(@PathVariable("id") String id,
			@RequestBody QuestaoObjetiva questaoObj) {
		QuestaoObjetiva updatedQuestaoObj = questaoObjService.update(questaoObj, id);
		return new ResponseEntity<QuestaoObjetiva>(updatedQuestaoObj, HttpStatus.OK);
	}

	@ApiOperation("Fornece um array de objetos do tipo questão objetiva registrados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoObjetiva.class) })
	@RequestMapping(value = "/questaoObj", method = RequestMethod.GET)
	public List<QuestaoObjetiva> getAll() {
		return questaoObjService.getAll();
	}

	@ApiOperation("Fornece os dados de uma questão objetiva em particular. O objeto contém: tipo, enunciado, fonte, autor, imagem, competencias e alternativas.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoObjetiva.class) })
	@RequestMapping(value = "/questaoObj/{id}", method = RequestMethod.GET) //// verificar se faz sentido
	public QuestaoObjetiva getById(@PathVariable("id") String id) {
		return questaoObjService.getById(id);
	}

}
