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

import springboot.model.QuestaoObjetiva;
import springboot.service.QuestaoObjetivaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class QuestaoObjetivaController {
	
	@Autowired
	QuestaoObjetivaService questaoObjService;
	
	@RequestMapping(value = "/questaoObj", method = RequestMethod.POST)
	public QuestaoObjetiva save(@RequestBody QuestaoObjetiva questaoObj) {
		return questaoObjService.save(questaoObj);
	}
	
	@RequestMapping(value = "/questaoObj/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<QuestaoObjetiva> delete(@PathVariable("id") Long id) {
		QuestaoObjetiva questaoObj = questaoObjService.delete(id);
		return new ResponseEntity<QuestaoObjetiva>(questaoObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/questaoObj/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoObjetiva> update(@PathVariable("id") Long id, @RequestBody QuestaoObjetiva questaoObj) {
		QuestaoObjetiva updatedQuestaoObj = questaoObjService.update(questaoObj, id);
		return new ResponseEntity<QuestaoObjetiva>(updatedQuestaoObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/questaoObj", method = RequestMethod.GET)
	public List<QuestaoObjetiva> getAll() {
		return questaoObjService.getAll();
	}
	
	@RequestMapping(value = "/questaoObj/{id}", method = RequestMethod.GET)    //// verificar se faz sentido
	public QuestaoObjetiva getById(@PathVariable("id") Long id) {
		return questaoObjService.getById(id);
	}

}
