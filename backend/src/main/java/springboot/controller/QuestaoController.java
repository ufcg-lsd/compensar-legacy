package springboot.controller;


import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.dto.input.QuestaoInput;
import springboot.enums.CompetenciaType;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.QuestaoService;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "QuestaoControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestaoController {

	@Autowired
	QuestaoService questaoService;

	@ApiOperation("Permite registrar uma nova questão no sistema. Requer que o corpo do request contenha um objeto com os campos: tipo, enunciado, fonte, autor, imagem, conteudo, espelho ou alternativas.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao", method = RequestMethod.POST)
	public Questao save(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody QuestaoInput questao) throws IOException {
		return questaoService.save(
				new Questao(
						questao.getTipo(),
						questao.getConteudo(),
						questao.getEnunciado(),
						questao.getFonte(),
						usuario.getNome(),
						questao.getEspelho(),
						questao.getAlternativas(),
						questao.getCompetencias()
				)
		);
	}

	@ApiOperation("Permite apagar uma questão do sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Questao> delete(@PathVariable("id") String id) {
		Questao questao = questaoService.delete(id);
		return new ResponseEntity<Questao>(questao, HttpStatus.OK);
	}

	@ApiOperation("Permite atualizar uma questão do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma questão subjetiva.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Questao> update(@PathVariable("id") String id, @RequestAttribute(name="usuario") Usuario usuario, @RequestBody QuestaoInput questao) throws IOException {
		Questao updatedQuestao = questaoService.update(
				new Questao(
						questao.getTipo(),
						questao.getConteudo(),
						questao.getEnunciado(),
						questao.getFonte(),
						usuario.getNome(),
						questao.getEspelho(),
						questao.getAlternativas(),
						questao.getCompetencias()
				),
				id
		);
		return new ResponseEntity<Questao>(updatedQuestao, HttpStatus.OK);
	}


	@ApiOperation("Retorna a(s) competência(s) para o enunciado.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK") })
	@RequestMapping(value = "/competencias", method = RequestMethod.POST)
	public Set<CompetenciaType> getSetCompetencias(@RequestBody String enunciado) throws IOException {
		
		return questaoService.getSetCompetencias(enunciado);
	}
	
	

}
