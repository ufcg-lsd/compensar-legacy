package springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.model.Alternativa;
import springboot.model.Questao;
import springboot.model.QuestaoCompetencia;
import springboot.model.Usuario;
import springboot.service.QuestaoCompetenciaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class QuestaoCompetenciaController {
	
	@Autowired
	QuestaoCompetenciaService questaoCompetenciaService;
	
	@ApiOperation("Permite adicionar uma competência à uma questão.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = QuestaoCompetencia.class)})
	@RequestMapping(value = "/questaoCompetencia", method = RequestMethod.POST)
	public QuestaoCompetencia save(@RequestBody QuestaoCompetencia questaoCompetencia) {

		return questaoCompetenciaService.save(questaoCompetencia);
	}
	
	@ApiOperation("Permite apagar uma competência relacionada à uma questão.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = QuestaoCompetencia.class)})
	@RequestMapping(value = "/questaoCompetencia/{id_questao}/{competencia}", method = RequestMethod.DELETE)
	public ResponseEntity<QuestaoCompetencia> delete(@PathVariable("id_questao") Long id_questao, @PathVariable("competencia") String competencia) {
		QuestaoCompetencia questaoCompetencia = questaoCompetenciaService.delete(id_questao,competencia);
		return new ResponseEntity<QuestaoCompetencia>(questaoCompetencia, HttpStatus.OK);
	}
	
	@ApiOperation("Permite atualizar uma competência relacionada à uma questão no sistema. Requer que o corpo do request contenha um objeto com os atributos de uma QuestaoCompetencia.\r\n" + 
			"")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = QuestaoCompetencia.class)})
	@RequestMapping(value = "/questaoCompetencia/{id_questao}/{competencia}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoCompetencia> update(@PathVariable("id_questao") Long id_questao, @PathVariable("competencia") String competencia, @RequestBody QuestaoCompetencia questaoCompetencia) {
		QuestaoCompetencia updatedQuestaoCompetencia = questaoCompetenciaService.update(id_questao,competencia,questaoCompetencia);
		return new ResponseEntity<QuestaoCompetencia>(updatedQuestaoCompetencia, HttpStatus.OK);
	}
	
	@ApiOperation("Fornece um array das questões cadastradas no sistema, e suas respectivas competências.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = QuestaoCompetencia.class)})
	@RequestMapping(value = "/questaoCompetencia", method = RequestMethod.GET)
	public List<QuestaoCompetencia> getAll() {
		return questaoCompetenciaService.getAll();
	}
		
	
}
	
	/**
	@RequestMapping(value = "/questaoCompetencia/add/{id_questao}/{id_competencia}", method = RequestMethod.PUT)
	public ResponseEntity<Questao> addCompetencia(@PathVariable("id") Long id_questao, @PathVariable("id_competencia") Long id_competencia) {
		Questao updatedQuestao = questaoCompetenciaService.addCompetencia(id_competencia, id_questao);
		return new ResponseEntity<Questao>(updatedQuestao, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/questaoCompetencia/remove/{id_questao}/{id_competencia}", method = RequestMethod.PUT)
	public ResponseEntity<Questao> removeCompetencia(@PathVariable("id") Long id_questao, @PathVariable("id_competencia") Long id_competencia) {
		Questao questao = questaoCompetenciaService.removeCompetencia(id_competencia, id_questao);
		return new ResponseEntity<Questao>(questao, HttpStatus.OK);
	}
	
	

*/