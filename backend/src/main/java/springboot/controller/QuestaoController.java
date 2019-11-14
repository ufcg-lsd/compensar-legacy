package springboot.controller;


import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.dto.IO.QuestaoIO;
import springboot.dto.input.AvaliacaoInput;
import springboot.dto.input.QuestaoInput;
import springboot.dto.output.QuestaoOutput;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;
import springboot.exception.data.PermissionDeniedException;
import springboot.model.Avaliacao;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.AvaliacaoService;
import springboot.service.QuestaoService;
import springboot.service.UsuarioService;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "QuestaoControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestaoController {

	@Autowired
	QuestaoService questaoService;

    @Autowired
    AvaliacaoController avaliacaoController;

	@Autowired
	AvaliacaoService avaliacaoService;

	@Autowired
	UsuarioService usuarioService;

	private QuestaoOutput convert(Questao questao, Usuario usuario) {
		return QuestaoIO.convert(questao, usuario, usuarioService, avaliacaoService);
	}

	@ApiOperation("Permite registrar uma nova questão no sistema. Requer que o corpo do request contenha um objeto com os campos: tipo, enunciado, fonte, autor, imagem, conteudo, espelho ou alternativas.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao", method = RequestMethod.POST)
	public QuestaoOutput save(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody QuestaoInput questao) throws IOException {
		Questao questaoSalva =  questaoService.save(QuestaoIO.convert(questao, usuario));
		avaliacaoController.save(usuario,
                new AvaliacaoInput(
                        questao.getObsAvaliacao(),
                        "",
                        questaoSalva.getId(),
                        questao.getCompetenciasAvaliacao(),
                        questao.getConfiancaAvaliacao()
                )
        );
		return convert(questaoSalva, usuario);
	}

	@ApiOperation("Permite apagar uma questão do sistema.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<QuestaoOutput> delete(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("id") String id) {
		Questao questao = questaoService.getById(id);
		if (!questao.getAutor().equals(usuario.getEmail())) {
			throw new PermissionDeniedException("A questão é de propriedade de outro usuário");
		}
		if (!questao.getEstado().equals(EstadoQuestao.RASCUNHO)) {
			throw new PermissionDeniedException("Apenas questões rascunho podem ser removidas");
		}
		questao = questaoService.delete(id);
		return new ResponseEntity<QuestaoOutput>(convert(questao, usuario), HttpStatus.OK);
	}

	@ApiOperation("Permite atualizar uma questão do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma questão subjetiva.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoOutput> update(@PathVariable("id") String id, @RequestAttribute(name="usuario") Usuario usuario, @RequestBody QuestaoInput questao) throws IOException {
		Questao q = QuestaoIO.convert(questao, usuario);
		if (!q.getEstado().equals(EstadoQuestao.RASCUNHO)) {
			throw new PermissionDeniedException("Apenas questões rascunho podem ser editadas");
		}
		Questao updatedQuestao = questaoService.update(q, id);

		return new ResponseEntity<QuestaoOutput>(convert(updatedQuestao, usuario), HttpStatus.OK);
	}


	@ApiOperation("Retorna a(s) competência(s) para o enunciado.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK") })
	@RequestMapping(value = "/competencias", method = RequestMethod.POST)
	public Set<CompetenciaType> getSetCompetencias(@RequestBody String enunciado) throws IOException {
		return questaoService.getSetCompetencias(enunciado);
	}

	@ApiOperation("Fornece um array de objetos do tipo questão correspondente às questões pendente de resposta para o usuário.\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/pendente/", method = RequestMethod.GET)
	public QuestaoOutput getAllPendentes(@RequestAttribute(name="usuario") Usuario usuario) throws IOException {
		return convert(questaoService.getPendente(usuario), usuario);
	}

	@ApiOperation("Permite enviar questão para avaliação.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/publish/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoOutput> publish(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("id") String id) throws IOException {
		Questao questao = questaoService.getById(id);
		if (!questao.getAutor().equals(usuario.getEmail())) {
			throw new PermissionDeniedException("A questão é de propriedade de outro usuário");
		}
		if (!questao.getEstado().equals(EstadoQuestao.RASCUNHO)) {
			throw new PermissionDeniedException("Apenas questões rascunho podem ser publicadas");
		}
		questao.setEstado(EstadoQuestao.PEND_AVALIACAO);
		questao = questaoService.update(questao, id);
		return new ResponseEntity<QuestaoOutput>(convert(questao, usuario), HttpStatus.OK);
	}
}
