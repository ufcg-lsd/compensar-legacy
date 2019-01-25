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
import springboot.model.ListaQuestoes;
import springboot.model.Questao;
import springboot.service.ListaQuestoesService;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class ListaQuestoesController {
	
	@Autowired
	ListaQuestoesService listaQuestoesService;
	
	@ApiOperation("Permite registrar uma nova lista de questões no sistema. Requer que o corpo do request contenha um objeto com os campos: email e questoes.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes", method = RequestMethod.POST)
	public ListaQuestoes save(@RequestBody ListaQuestoes listaQuestoes) {
		return listaQuestoesService.save(listaQuestoes);
	}
	
	@ApiOperation("Permite apagar listas de questões da autoria de determinado email no sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/{email}", method = RequestMethod.DELETE)
	public ResponseEntity<ListaQuestoes> delete(@PathVariable("email") String email) {
		ListaQuestoes listaQuestoes = listaQuestoesService.delete(email);
		return new ResponseEntity<ListaQuestoes>(listaQuestoes, HttpStatus.OK);
	}

	@ApiOperation("Permite apagar uma lista de questões no sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ListaQuestoes> deleteByID(@PathVariable("id") Long id) {
		ListaQuestoes listaQuestoes = listaQuestoesService.deleteByID(id);
		return new ResponseEntity<ListaQuestoes>(listaQuestoes, HttpStatus.OK);
	}
	
	@ApiOperation("Permite atualizar uma lista de questões do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma lista de questões.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/{email}", method = RequestMethod.PUT)
	public ResponseEntity<ListaQuestoes> update(@PathVariable("email") String email,
			@RequestBody ListaQuestoes listaQuestoes) {
		ListaQuestoes updatedlistaQuestoes = listaQuestoesService.update(listaQuestoes, email);
		return new ResponseEntity<ListaQuestoes>(updatedlistaQuestoes, HttpStatus.OK);
	}

	@ApiOperation("Fornece um array de objetos do tipo lista de questões registrados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes", method = RequestMethod.GET)
	public List<ListaQuestoes> getAll() {
		return listaQuestoesService.getAll();
	}
	
	
	@ApiOperation("Fornece os dados de lista de questões registradas por determinado email. \r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/{email}", method = RequestMethod.GET)
	public ListaQuestoes getByEmail(@PathVariable("email") String email) {
		return listaQuestoesService.getByEmail(email);
	} 

}
