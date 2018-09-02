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

import springboot.model.Competencia;
import springboot.service.CompetenciaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class CompetenciaController {
	
	@Autowired
	CompetenciaService competenciaService;
	
	@RequestMapping(value = "/competencia", method = RequestMethod.POST)
	public Competencia save(@RequestBody Competencia competencia) {
		return competenciaService.save(competencia);
	}
	
	@RequestMapping(value = "/competencia/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Competencia> delete(@PathVariable("id") Long id) {
		Competencia competencia = competenciaService.delete(id);
		return new ResponseEntity<Competencia>(competencia, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/competencia/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Competencia> update(@PathVariable("id") Long id, @RequestBody Competencia competencia) {
		Competencia updatedCompetencia = competenciaService.update(competencia, id);
		return new ResponseEntity<Competencia>(updatedCompetencia, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/competencia", method = RequestMethod.GET)
	public List<Competencia> getAll() {
		return competenciaService.getAll();
	}
	
	@RequestMapping(value = "/competencia/{id}", method = RequestMethod.GET)   
	public Competencia getById(@PathVariable("id") Long id) {
		return competenciaService.getById(id);
	}
	
	

}
