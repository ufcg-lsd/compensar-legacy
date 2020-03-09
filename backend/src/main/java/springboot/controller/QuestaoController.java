package springboot.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import springboot.enums.AvaliacaoPublicacao;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;
import springboot.enums.PermissaoType;
import springboot.exception.data.PermissionDeniedException;
import springboot.model.Avaliacao;
import springboot.model.Conteudo;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.AvaliacaoService;
import springboot.service.ConteudoService;
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

	@Autowired
	ConteudoService conteudoService;

	private QuestaoOutput convert(Questao questao, Usuario usuario, boolean forceAvaliacoes) {
		return QuestaoIO.convert(questao, usuario, usuarioService, avaliacaoService, questaoService, forceAvaliacoes);
	}

	@ApiOperation("Permite registrar uma nova questão no sistema. Requer que o corpo do request contenha um objeto com os campos: tipo, enunciado, fonte, autor, imagem, conteudo, espelho ou alternativas.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao", method = RequestMethod.POST)
	public QuestaoOutput save(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody QuestaoInput questao) throws IOException {
		Questao questaoSalva =  questaoService.save(QuestaoIO.convert(questao, usuario.getEmail()));
		avaliacaoController.save(usuario,
				new AvaliacaoInput(
						questao.getObsAvaliacao(),
						"",
						questaoSalva.getId(),
						questao.getCompetenciasAvaliacao(),
						questao.getInfoCompetenciasAvaliacao(),
						questao.getConfiancaAvaliacao(),
						AvaliacaoPublicacao.PRONTA
				)
		);
		return convert(questaoSalva, usuario, false);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Conteudo.class) })
	@RequestMapping(value = "/conteudo", method = RequestMethod.POST)
	public Conteudo saveConteudo(@RequestBody String conteudo) throws IOException {
		conteudoService.save(new Conteudo(conteudo));
		return new Conteudo(conteudo);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Conteudo.class) })
	@RequestMapping(value = "/conteudo", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getAllConteudo() throws IOException {
		List<String> l = new ArrayList<>();
		for (Conteudo conteudoItem : conteudoService.getAll()) {
			if (!conteudoItem.getNome().equals("Outros"))
				l.add(conteudoItem.getNome());
		}
		l.add("Outros");

		return new ResponseEntity<List<String>>(l, HttpStatus.OK);
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
		return new ResponseEntity<QuestaoOutput>(convert(questao, usuario, false), HttpStatus.OK);
	}

	@ApiOperation("Permite atualizar uma questão do sistema. Requer que o corpo do request contenha um objeto com os atributos de uma questão subjetiva.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoOutput> update(@PathVariable("id") String id, @RequestAttribute(name="usuario") Usuario usuario, @RequestBody QuestaoInput questao) throws IOException {
		Questao q = QuestaoIO.convert(questao, usuario.getEmail());
		q.setQtdAvaliacoes(1);
		Questao oldQuestion = questaoService.getById(id);
		if (!oldQuestion.getEstado().equals(EstadoQuestao.RASCUNHO)) {
			throw new PermissionDeniedException("Uma questão definitiva não pode ser mais alterada");
		}
		Questao updatedQuestao = questaoService.update(q, id);


		return new ResponseEntity<QuestaoOutput>(convert(updatedQuestao, usuario, false), HttpStatus.OK);
	}


	@ApiOperation("Retorna a(s) competência(s) para o enunciado.\r\n"
			+ "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK") })
	@RequestMapping(value = "/competencias", method = RequestMethod.POST)
	public Set<CompetenciaType> getSetCompetencias(@RequestBody String enunciado) throws IOException {
		return questaoService.getSetCompetencias(enunciado);
	}

	@ApiOperation("Fornece uma questão pendente de avaliação para o usuário.\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/pendente/", method = RequestMethod.GET)
	public QuestaoOutput getPendente(@RequestAttribute(name="usuario") Usuario usuario) throws IOException {
		return convert(questaoService.getPendente(usuario), usuario, false);
	}

	@ApiOperation("Fornece uma questão pendente de aprovação para publicação para o usuário.\r\n" + "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/avaliada/", method = RequestMethod.GET)
	public QuestaoOutput getAvaliada(@RequestAttribute(name="usuario") Usuario usuario) throws IOException {
		return convert(questaoService.getAvaliada(), usuario, true);
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
		return new ResponseEntity<QuestaoOutput>(convert(questao, usuario, false), HttpStatus.OK);
	}

	@ApiOperation("Aprovar questão já avaliada.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/aprove/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoOutput> aprovarQuestao(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("id") String id, @RequestBody QuestaoInput novaQuestaoInput) throws IOException {
		Questao questao = questaoService.getById(id);
		Questao novaQuestao = QuestaoIO.convert(novaQuestaoInput, questao.getAutor());

		if (!usuario.getPermissoes().contains(PermissaoType.JUDGE)) {
			throw new PermissionDeniedException("Apenas um usuário com permissão de juiz pode aprovar/reprovar uma questão");
		}
		if (!questao.getEstado().equals(EstadoQuestao.PEND_APROVACAO)) {
			throw new PermissionDeniedException("Apenas questões pendentes de apovação podem ser aprovadas/reprovadas");
		}
		novaQuestao.setEstado(EstadoQuestao.PUBLICADA);
		questao = questaoService.update(novaQuestao, id);
		return new ResponseEntity<QuestaoOutput>(convert(questao, usuario, false), HttpStatus.OK);
	}

	@ApiOperation("Rejeitar questão já avaliada.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Questao.class) })
	@RequestMapping(value = "/questao/reject/{id}", method = RequestMethod.PUT)
	public ResponseEntity<QuestaoOutput> rejeitarQuestao(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("id") String id) throws IOException {
		Questao questao = questaoService.getById(id);
		if (!usuario.getPermissoes().contains(PermissaoType.JUDGE)) {
			throw new PermissionDeniedException("Apenas um usuário com permissão de juiz pode aprovar/reprovar uma questão");
		}
		if (!questao.getEstado().equals(EstadoQuestao.PEND_APROVACAO)) {
			throw new PermissionDeniedException("Apenas questões pendentes de apovação podem ser aprovar/reprovar");
		}
		questao.setEstado(EstadoQuestao.REJEITADA);
		questao = questaoService.update(questao, id);
		return new ResponseEntity<QuestaoOutput>(convert(questao, usuario, false), HttpStatus.OK);
	}
}
