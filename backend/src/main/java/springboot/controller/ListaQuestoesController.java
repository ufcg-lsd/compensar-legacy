package springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.dto.IO.ListaQuestoesIO;
import springboot.dto.input.ListaQuestoesInput;
import springboot.dto.input.QuestaoInput;
import springboot.dto.output.ListaQuestoesOutput;
import springboot.model.ListaQuestoes;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.ListaQuestoesService;
import springboot.service.QuestaoService;
import springboot.service.UsuarioService;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
public class ListaQuestoesController {
	
	@Autowired
	ListaQuestoesService listaQuestoesService;

	@Autowired
	QuestaoService questaoService;
	
	@ApiOperation("Permite registrar uma nova lista de questões no sistema. Requer que o corpo do request contenha um objeto com os campos: email e questoes.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes", method = RequestMethod.POST)
	public ListaQuestoes save(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody ListaQuestoesInput listaQuestoes) {
		return listaQuestoesService.save(ListaQuestoesIO.convert(listaQuestoes, usuario, questaoService));
	}
	
	@ApiOperation("Permite apagar uma lista de questões no sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ListaQuestoes> delete(@PathVariable("id") String id) {
		ListaQuestoes listaQuestoes = listaQuestoesService.delete(id);
		return new ResponseEntity<ListaQuestoes>(listaQuestoes, HttpStatus.OK);
	}
	
	@ApiOperation("Permite atualizar uma lista de questões do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma lista de questões.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ListaQuestoes> update(@PathVariable("id") String id, @RequestAttribute(name="usuario") Usuario usuario, @RequestBody ListaQuestoesInput listaQuestoes) {
		ListaQuestoes updatedlistaQuestoes = listaQuestoesService.update(ListaQuestoesIO.convert(listaQuestoes, usuario, questaoService), id);
		return new ResponseEntity<ListaQuestoes>(updatedlistaQuestoes, HttpStatus.OK);
	}

	@ApiOperation("Fornece um array de objetos do tipo lista de questões registrados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/{page}/{size}", method = RequestMethod.GET)
	public Page<ListaQuestoes> getAll(@RequestAttribute(name="usuario") Usuario usuario,@PathVariable("page") int page,@PathVariable("size") int size) {
		return listaQuestoesService.getAll(usuario, page, size);
	}
	
	@ApiOperation("Fornece a lista de questões com o id especificado.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/{id}", method = RequestMethod.GET)
	public ListaQuestoes getById(@PathVariable("id") String id) {
		return listaQuestoesService.getById(id);
	}


/*
	@ApiOperation("Fornece os dados de lista de questões registradas por determinado email. \r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/listaquestoes/{page}/{size}", method = RequestMethod.GET)
	public Page<ListaQuestoesOutput> getByNomeEmail(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("email") String email,
													@PathVariable("page") int page, @PathVariable("size") int size) {
		Page<ListaQuestoes> listas = listaQuestoesService.getByUser(usuario,page,size);
		return listas.map(ListaQuestoesIO::convert);

	}
*/
}
