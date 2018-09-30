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
import springboot.model.Alternativa;
import springboot.service.AlternativaService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "AlternativaControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlternativaController {

	@Autowired
	AlternativaService alternativaService;

	@ApiOperation("Permite apagar uma alternativa do sistema, consequentemente, da questão que estava associada. \n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Alternativa.class) })
	@RequestMapping(value = "/alternativa/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Alternativa> delete(@PathVariable("id") Long id) {
		Alternativa alternativa = alternativaService.delete(id);
		return new ResponseEntity<Alternativa>(alternativa, HttpStatus.OK);
	}

	@ApiOperation("Permite atualizar uma alternativa do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma alternativa, no formato:  \"<atributo>:<valor>\".\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Alternativa.class) })
	@RequestMapping(value = "/alternativa/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Alternativa> update(@PathVariable("id") Long id, @RequestBody Alternativa alternativa) {
		Alternativa updatedAlternativa = alternativaService.update(alternativa, id);
		return new ResponseEntity<Alternativa>(updatedAlternativa, HttpStatus.OK);
	}

	@ApiOperation("Fornece os dados de uma alternativa em particular. O objeto contém: texto, correta, id_alternativa. O campo correta indica de forma booleana se a alternativa é a correta ou não.\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Alternativa.class) })
	@RequestMapping(value = "/alternativa/{id}", method = RequestMethod.GET)
	public Alternativa getById(@PathVariable("id") Long id) {
		return alternativaService.getById(id);
	}

	@ApiOperation("Fornece um array de objetos do tipo alternativa registrados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Alternativa.class) })
	@RequestMapping(value = "/alternativa", method = RequestMethod.GET)
	public List<Alternativa> getAll() {
		return alternativaService.getAll();
	}

}
