package springboot.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import springboot.dto.input.AvaliacaoInput;
import springboot.enums.CompetenciaType;
import springboot.enums.EstadoQuestao;
import springboot.model.Avaliacao;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.AvaliacaoService;
import springboot.service.QuestaoService;

/**
 * Controlador responsável por gerenciar operações relacionadas às avaliações no sistema.
 * Permite registrar novas avaliações para uma questão específica e atualizar o estado
 * da questão com base nas avaliações recebidas.
 */
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
@Tag(name = "AvaliacaoController", description = "API para gerenciar avaliações")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Autowired
    private QuestaoService questaoService;

    /**
     * Registra uma nova avaliação para uma questão específica. A avaliação inclui
     * observações, competências avaliadas e informações adicionais. Caso a questão
     * atinja três avaliações, o sistema calcula as competências em comum e atualiza
     * o estado da questão para "PEND_APROVACAO".
     * 
     * @param usuario o usuário que está realizando a avaliação, obtido a partir de um atributo da requisição.
     * @param avaliacao os dados da avaliação, enviados no corpo da requisição.
     * @return uma resposta HTTP com a avaliação criada ou com o código de status adequado 
     *         em caso de erro (404 se a questão não for encontrada, 201 para sucesso).
     * @throws IOException em caso de erro durante o processamento da avaliação.
     */
    @Operation(summary = "Permite registrar uma nova avaliação no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso", 
                         content = @Content(mediaType = "application/json", 
                         schema = @Schema(implementation = Avaliacao.class))),
            @ApiResponse(responseCode = "404", description = "Questão não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content)
    })
    @PostMapping("/avaliacao")
    public ResponseEntity<Avaliacao> save(@RequestAttribute(name = "usuario") Usuario usuario,
            @Valid @RequestBody AvaliacaoInput avaliacao) throws IOException {

        Questao questao = questaoService.getById(avaliacao.getQuestaoId());
        if (questao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Avaliacao novaAvaliacao = avaliacaoService.save(
                new Avaliacao(
                        avaliacao.getObservacaoAvaliacao(),
                        avaliacao.getObservacaoQuestao(),
                        avaliacao.getCompetencias(),
                        avaliacao.getInfoCompetencias(),
                        usuario.getEmail(),
                        questao.getId(),
                        avaliacao.getConfianca(),
                        avaliacao.getAvaliacaoPublicacao()));

        questao.setQtdAvaliacoes(questao.getQtdAvaliacoes() + 1);

        if (questao.getQtdAvaliacoes() == 3) {
            HashSet<CompetenciaType> novasCompetencias = calcularCompetenciasComuns(avaliacao.getQuestaoId());
            questao.setCompetencias(novasCompetencias);
            questao.setEstado(EstadoQuestao.PEND_APROVACAO);
        }

        questaoService.update(questao, questao.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }

    /**
     * Calcula as competências comuns entre as avaliações de uma determinada questão.
     * Para que uma competência seja considerada comum, ela deve estar presente em
     * pelo menos duas das três avaliações realizadas.
     * 
     * @param questaoId o ID da questão para a qual as avaliações estão sendo processadas.
     * @return um conjunto contendo as competências comuns identificadas entre as avaliações.
     */
    private HashSet<CompetenciaType> calcularCompetenciasComuns(String questaoId) {
        List<Avaliacao> avaliacoes = avaliacaoService.getAllByQuestao(questaoId);
        HashSet<CompetenciaType> competenciasComuns = new HashSet<>();

        for (CompetenciaType competencia : CompetenciaType.values()) {
            int contador = 0;
            for (Avaliacao avaliacao : avaliacoes) {
                if (avaliacao.getCompetencias().contains(competencia)) {
                    contador++;
                }
            }
            if (contador >= 2) {
                competenciasComuns.add(competencia);
            }
        }
        return competenciasComuns;
    }
}
