package springboot.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.dto.IO.QuestaoIO;
import springboot.dto.output.QuestaoOutput;
import springboot.enums.EstadoQuestao;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.AvaliacaoService;
import springboot.service.QuestaoService;
import springboot.service.UsuarioService;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "QuestaoSearchControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionSearchController {
	
	@Autowired
	QuestaoService questaoService;

	@Autowired
	AvaliacaoService avaliacaoService;

	@Autowired
	UsuarioService usuarioService;

	private QuestaoOutput convert(Questao questao, Usuario usuario) {
		return QuestaoIO.convert(questao, usuario, usuarioService, avaliacaoService, false);
	}

	
	@ApiOperation("Fornece um array de objetos do tipo questão registrados.\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{page}/{size}", method = RequestMethod.GET)
	public Page<QuestaoOutput> getAll(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("page") int page, @PathVariable("size") int size) {
		return questaoService.getAll(page,size).map(q -> this.convert(q, usuario));
	}

	@ApiOperation("Fornece os dados de uma questão em particular. O objeto contém: tipo, enunciado, fonte, autor, imagem, competencias e espelho.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.GET)
	public QuestaoOutput getById(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("id") String id) {
		return convert(questaoService.getById(id), usuario);
	}

	
	@ApiOperation("Fornece um array de questões que fazem o match com o enunciado, competências (cada uma entre aspas), "
			+ "autor, fonte e tipo\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/search/{enunciado}/{competencias}/{autor}/{fonte}/{tipo}/{conteudo}/{page}/{size}", method = RequestMethod.GET)
	public Page<QuestaoOutput> getByEnunciadoCompetenciasAutorFonteTipo(@RequestAttribute(name="usuario") Usuario usuario,
			@PathVariable("enunciado") String enunciado,
			@PathVariable("competencias") HashSet<String> competencias,@PathVariable("autor") String autor,
			@PathVariable("fonte") String fonte, @PathVariable("tipo") String tipo, @PathVariable("conteudo") String conteudo,
			@PathVariable("page") int page,@PathVariable("size") int size) {
		System.out.println(competencias);
		Set<EstadoQuestao> estados = new HashSet<>();
		estados.add(EstadoQuestao.PUBLICADA);
		return questaoService.getByEnunciadoCompetenciasAutorFonteTipo(enunciado, competencias, autor, "", fonte, tipo,conteudo, estados, page,size).map(q -> this.convert(q, usuario));
	}

	@ApiOperation("Fornece um array de questões que fazem o match com o enunciado, competências (cada uma entre aspas), "
			+ "autor, fonte e tipo\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/searchMy/{enunciado}/{competencias}/{estados}/{fonte}/{tipo}/{conteudo}/{page}/{size}", method = RequestMethod.GET)
	public Page<QuestaoOutput> getMyQuestionsByEnunciadoCompetenciasAutorFonteTipo(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("enunciado") String enunciado,
																			 @PathVariable("competencias") HashSet<String> competencias, @PathVariable("estados") HashSet<EstadoQuestao> estados,
																			 @PathVariable("fonte") String fonte, @PathVariable("tipo") String tipo, @PathVariable("conteudo") String conteudo,
																			 @PathVariable("page") int page, @PathVariable("size") int size) {
		System.out.println(competencias);
		return questaoService.getByEnunciadoCompetenciasAutorFonteTipo(enunciado, competencias, "", usuario.getEmail(), fonte, tipo,conteudo, estados, page,size).map(q -> this.convert(q, usuario));
	}



}
