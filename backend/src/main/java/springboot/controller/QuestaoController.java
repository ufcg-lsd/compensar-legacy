package springboot.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springboot.dto.IO.QuestaoIO;
import springboot.dto.input.AvaliacaoInput;
import springboot.dto.input.QuestaoInput;
import springboot.dto.output.QuestaoOutput;
import springboot.enums.AvaliacaoPublicacao;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;
import springboot.enums.PermissaoType;
import springboot.exception.data.PermissionDeniedException;
import springboot.model.Conteudo;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.ConteudoService;
import springboot.service.QuestaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
@Tag(name = "QuestaoController", description = "Controlador de operações relacionadas a Questões")
public class QuestaoController {
    @Autowired
    private QuestaoService questaoService;

    @Autowired
    private AvaliacaoController avaliacaoController;

    @Autowired
    private ConteudoService conteudoService;

    @Autowired
    private QuestaoIO questaoIO;

    private QuestaoOutput convertToDto(Questao questao, Usuario usuario, boolean forceAvaliacoes) {
        return questaoIO.toDto(questao, usuario, forceAvaliacoes);
    }

    @PostMapping("/questao")
    @Operation(summary = "Permite registrar uma nova questão no sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestaoOutput.class)))
    })
    public QuestaoOutput saveQuestao(@RequestAttribute(name = "usuario") Usuario usuario,
            @RequestBody QuestaoInput questaoInput) throws IOException {
        Questao questaoSalva = questaoService.save(QuestaoIO.toEntity(questaoInput, usuario.getEmail()));
        AvaliacaoInput avaliacaoInput = new AvaliacaoInput(
                questaoInput.getObsAvaliacao(), "", questaoSalva.getId(),
                questaoInput.getCompetenciasAvaliacao(), questaoInput.getInfoCompetenciasAvaliacao(),
                questaoInput.getConfiancaAvaliacao(), AvaliacaoPublicacao.PRONTA);
        avaliacaoController.save(usuario, avaliacaoInput);
        return convertToDto(questaoSalva, usuario, false);
    }

    @PostMapping("/conteudo")
    @Operation(summary = "Permite salvar um novo conteúdo no sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Conteudo.class)))
    })
    public ResponseEntity<Conteudo> saveConteudo(@RequestBody String conteudo) {
        Conteudo conteudoSalvo = conteudoService.save(new Conteudo(conteudo));
        return ResponseEntity.ok(conteudoSalvo);
    }

    @GetMapping("/conteudo")
    @Operation(summary = "Recupera todos os conteúdos do sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<List<String>> getAllConteudo() {
        List<String> conteudos = conteudoService.getAll().stream()
                .map(Conteudo::getNome)
                .filter(nome -> !nome.equals("Outros"))
                .collect(Collectors.toList());
        conteudos.add("Outros");
        return ResponseEntity.ok(conteudos);
    }

    @DeleteMapping("/questao/{id}")
    @Operation(summary = "Permite apagar uma questão do sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestaoOutput.class)))
    })
    public ResponseEntity<QuestaoOutput> deleteQuestao(@RequestAttribute(name = "usuario") Usuario usuario,
            @PathVariable("id") String id) {
        Questao questao = questaoService.getById(id);
        validateUserAuthorization(usuario, questao);
        validateQuestaoState(questao, EstadoQuestao.RASCUNHO, "Apenas questões rascunho podem ser removidas");

        Questao deletedQuestao = questaoService.delete(id);
        return ResponseEntity.ok(convertToDto(deletedQuestao, usuario, false));
    }

    @PutMapping("/questao/{id}")
    @Operation(summary = "Permite atualizar uma questão do sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestaoOutput.class)))
    })
    public ResponseEntity<QuestaoOutput> updateQuestao(@PathVariable("id") String id,
            @RequestAttribute(name = "usuario") Usuario usuario, @RequestBody QuestaoInput questaoInput)
            throws IOException {
        Questao oldQuestao = questaoService.getById(id);
        validateQuestaoState(oldQuestao, EstadoQuestao.RASCUNHO, "Uma questão definitiva não pode ser mais alterada");

        Questao updatedQuestao = questaoService.update(QuestaoIO.toEntity(questaoInput, usuario.getEmail()), id);
        return ResponseEntity.ok(convertToDto(updatedQuestao, usuario, false));
    }

    @PostMapping("/competencias")
    @Operation(summary = "Retorna a(s) competência(s) para o enunciado.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CompetenciaType.class)))
    })
    public Set<CompetenciaType> getCompetencias(@RequestBody String enunciado) throws IOException {
        return questaoService.getSetCompetencias(enunciado);
    }

    @GetMapping("/questao/pendente")
    @Operation(summary = "Fornece uma questão pendente de avaliação para o usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Questao.class)))
    })
    public QuestaoOutput getPendente(@RequestAttribute(name = "usuario") Usuario usuario) throws IOException {
        return convertToDto(questaoService.getPendente(usuario), usuario, false);
    }

    @GetMapping("/questao/avaliada")
    @Operation(summary = "Fornece uma questão pendente de aprovação para publicação para o usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestaoOutput.class)))
    })
    public QuestaoOutput getAvaliada(@RequestAttribute(name = "usuario") Usuario usuario) {
        return convertToDto(questaoService.getAvaliada(), usuario, true);
    }

    @PutMapping("/questao/publish/{id}")
    @Operation(summary = "Permite enviar questão para avaliação.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestaoOutput.class)))
    })
    public ResponseEntity<QuestaoOutput> publishQuestao(@RequestAttribute(name = "usuario") Usuario usuario,
            @PathVariable("id") String id) throws IOException {
        Questao questao = questaoService.getById(id);
        validateUserAuthorization(usuario, questao);
        validateQuestaoState(questao, EstadoQuestao.RASCUNHO, "Apenas questões rascunho podem ser publicadas");

        questao.setEstado(EstadoQuestao.PEND_AVALIACAO);
        Questao publishedQuestao = questaoService.update(questao, id);
        return ResponseEntity.ok(convertToDto(publishedQuestao, usuario, false));
    }

    @PutMapping("/questao/aprove/{id}")
    @Operation(summary = "Aprova uma questão já avaliada.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestaoOutput.class)))
    })
	public ResponseEntity<QuestaoOutput> aprovarQuestao(@RequestAttribute(name="usuario") Usuario usuario, @PathVariable("id") String id, @RequestBody QuestaoInput novaQuestaoInput) throws IOException {
		Questao questao = questaoService.getById(id);
		Questao novaQuestao = QuestaoIO.toEntity(novaQuestaoInput, questao.getAutor());

		if (!usuario.getPermissoes().contains(PermissaoType.JUDGE)) {
			throw new PermissionDeniedException("Apenas um usuário com permissão de juiz pode aprovar/reprovar uma questão");
		}
		if (!questao.getEstado().equals(EstadoQuestao.PEND_APROVACAO)) {
			throw new PermissionDeniedException("Apenas questões pendentes de apovação podem ser aprovadas/reprovadas");
		}
		novaQuestao.setEstado(EstadoQuestao.PUBLICADA);
		questao = questaoService.update(novaQuestao, id);
		return ResponseEntity.ok(convertToDto(questao, usuario, false));
	}

	@PutMapping("/questao/reject/{id}")
    @Operation(summary = "Permite rejeitar questão já avaliada.", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestaoOutput.class)))
    })
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
		return ResponseEntity.ok(convertToDto(questao, usuario, false));
	}

	@GetMapping("/updateClassificador")
    @Operation(summary = "Atualiza competenciaClassificador questão já avaliada", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestaoOutput.class)))
    })
	public ResponseEntity<Boolean> updateCompetencias(@RequestAttribute(name="usuario") Usuario usuario) throws IOException {
		if (!usuario.getPermissoes().contains(PermissaoType.JUDGE)) {
			throw new PermissionDeniedException("Apenas um usuário com permissão de juiz pode atualizar a classificação automática das questões");
		}
		return ResponseEntity.ok(questaoService.updateClassificador());
	}

    private void validateUserAuthorization(Usuario usuario, Questao questao) {
        if (!questao.getAutor().equals(usuario.getEmail())) {
            throw new PermissionDeniedException("A questão é de propriedade de outro usuário");
        }
    }

    private void validateQuestaoState(Questao questao, EstadoQuestao estado, String errorMessage) {
        if (!questao.getEstado().equals(estado)) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
