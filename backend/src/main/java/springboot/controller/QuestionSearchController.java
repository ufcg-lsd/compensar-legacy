package springboot.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.model.Questao;
import springboot.service.QuestaoService;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "QuestaoSearchControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionSearchController {
	
	@Autowired
	QuestaoService questaoService;
	
	@ApiOperation("Fornece um array de objetos do tipo questão registrados.\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{page}/{size}", method = RequestMethod.GET)
	public Page<Questao> getAll(@PathVariable("page") int page,@PathVariable("size") int size) {
		return questaoService.getAll(page,size);
	}

	@ApiOperation("Fornece os dados de uma questão em particular. O objeto contém: tipo, enunciado, fonte, autor, imagem, competencias e espelho.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.GET)
	public Questao getById(@PathVariable("id") String id) {
		return questaoService.getById(id);
	}

	
	@ApiOperation("Fornece um array de questões que fazem o match com o enunciado, competências (cada uma entre aspas), "
			+ "autor, fonte e tipo\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/search/{enunciado}/{competencias}/{autor}/{fonte}/{tipo}/{conteudo}/{page}/{size}", method = RequestMethod.GET)
	public Page<Questao> getByEnunciadoCompetenciasAutorFonteTipo(@PathVariable("enunciado") String enunciado,
			@PathVariable("competencias") HashSet<String> competencias,@PathVariable("autor") String autor,
			@PathVariable("fonte") String fonte, @PathVariable("tipo") String tipo, @PathVariable("conteudo") String conteudo,
			@PathVariable("page") int page,@PathVariable("size") int size) {
		System.out.println(competencias);
		return questaoService.getByEnunciadoCompetenciasAutorFonteTipo(enunciado, competencias,autor, fonte, tipo,conteudo,
				page,size);
	}
	


}
