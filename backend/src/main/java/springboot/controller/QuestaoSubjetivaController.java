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
import springboot.model.QuestaoSubjetiva;
import springboot.service.QuestaoSubjetivaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "QuestaoSubjControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestaoSubjetivaController {
	
	@Autowired
	QuestaoSubjetivaService questaoSubjService;
	
	@ApiOperation("Permite registrar uma nova questão subjetiva no sistema. Requer que o corpo do request contenha um objeto com os campos: tipo, enunciado, fonte, autor, imagem e espelho.\r\n" + 
			"")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoSubjetiva.class) })
	@RequestMapping(value = "/questaoSubj", method = RequestMethod.POST)
	public QuestaoSubjetiva save(@RequestBody QuestaoSubjetiva questaoSubj) {
		return questaoSubjService.save(questaoSubj);
	}
	
	@ApiOperation("Permite apagar uma questão subjetiva do sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoSubjetiva.class) })
	@RequestMapping(value = "/questaoSubj/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<QuestaoSubjetiva> delete(@PathVariable("id") Long id) {
		QuestaoSubjetiva questaoSubj = questaoSubjService.delete(id);
		return new ResponseEntity<QuestaoSubjetiva>(questaoSubj, HttpStatus.OK);
	}
	
	@ApiOperation("Permite atualizar uma questão subjetiva do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma questão subjetiva.\r\n" + 
			"")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoSubjetiva.class) })
	@RequestMapping(value = "/questaoSubj/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoSubjetiva> update(@PathVariable("id") Long id, @RequestBody QuestaoSubjetiva questaoSubj) {
		QuestaoSubjetiva updatedQuestaoSubj = questaoSubjService.update(questaoSubj, id);
		return new ResponseEntity<QuestaoSubjetiva>(updatedQuestaoSubj, HttpStatus.OK);
	}
	
	@ApiOperation("Fornece um array de objetos do tipo questão subjetiva registrados.\r\n" + 
			"")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoSubjetiva.class) })
	@RequestMapping(value = "/questaoSubj", method = RequestMethod.GET)
	public List<QuestaoSubjetiva> getAll() {
		return questaoSubjService.getAll();
	}
	
	@ApiOperation("Fornece os dados de uma questão subjetiva em particular. O objeto contém: tipo, enunciado, fonte, autor, imagem, competencias e espelho.\r\n" + 
			"")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = QuestaoSubjetiva.class) })
	@RequestMapping(value = "/questaoSubj/{id}", method = RequestMethod.GET)    
	public QuestaoSubjetiva getById(@PathVariable("id") Long id) {
		return questaoSubjService.getById(id);
	}
	
	// get by autor
	//get by tipo
	// get by fonte
	
	
	

}
