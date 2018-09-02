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

import springboot.model.Alternativa;
import springboot.service.AlternativaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class AlternativaController {
	
	@Autowired
	AlternativaService alternativaService;
	
	
	@RequestMapping(value = "/alternativa", method = RequestMethod.POST)
	public Alternativa save(@RequestBody Alternativa alternativa) {
		return alternativaService.save(alternativa);
	}
	
	@RequestMapping(value = "/alternativa/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Alternativa> delete(@PathVariable("id") Long id) {
		Alternativa alternativa = alternativaService.delete(id);
		return new ResponseEntity<Alternativa>(alternativa, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/alternativa/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Alternativa> update(@PathVariable("id") Long id, @RequestBody Alternativa alternativa) {
		Alternativa updatedAlternativa = alternativaService.update(alternativa, id);
		return new ResponseEntity<Alternativa>(updatedAlternativa, HttpStatus.OK);
	}


	@RequestMapping(value = "/alternativa/search/{id}", method = RequestMethod.GET)    //// verificar se faz sentido
	public Alternativa getById(@PathVariable("id") Long id) {
		return alternativaService.getById(id);
	}

	
	@RequestMapping(value = "/alternativa", method = RequestMethod.GET)
	public List<Alternativa> getAll() {
		return alternativaService.getAll();
	}
	
	

	
	
	
	
	
}
