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

import springboot.model.QuestaoSubjetiva;
import springboot.service.QuestaoSubjetivaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class QuestaoSubjetivaController {
	
	@Autowired
	QuestaoSubjetivaService questaoSubjService;
	
	@RequestMapping(value = "/questaoSubj", method = RequestMethod.POST)
	public QuestaoSubjetiva save(@RequestBody QuestaoSubjetiva questaoSubj) {
		return questaoSubjService.save(questaoSubj);
	}
	
	@RequestMapping(value = "/questaoSubj/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<QuestaoSubjetiva> delete(@PathVariable("id") Long id) {
		QuestaoSubjetiva questaoSubj = questaoSubjService.delete(id);
		return new ResponseEntity<QuestaoSubjetiva>(questaoSubj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/questaoSubj/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoSubjetiva> update(@PathVariable("id") Long id, @RequestBody QuestaoSubjetiva questaoSubj) {
		QuestaoSubjetiva updatedQuestaoSubj = questaoSubjService.update(questaoSubj, id);
		return new ResponseEntity<QuestaoSubjetiva>(updatedQuestaoSubj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/questaoSubj", method = RequestMethod.GET)
	public List<QuestaoSubjetiva> getAll() {
		return questaoSubjService.getAll();
	}
	
	@RequestMapping(value = "/questaoSubj/search/{id}", method = RequestMethod.GET)    //// verificar se faz sentido
	public QuestaoSubjetiva getById(@PathVariable("id") Long id) {
		return questaoSubjService.getById(id);
	}
	
	// get by autor
	//get by tipo
	// get by fonte
	
	
	

}
