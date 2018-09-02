package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springboot.model.Questao;
import springboot.service.QuestaoCompetenciaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class QuestaoCompetenciaController {
	
	@Autowired
	QuestaoCompetenciaService questaoCompetenciaService;
	
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
	
	
	
	
	

}
