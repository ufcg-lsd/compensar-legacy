package springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springboot.dto.input.AvaliacaoInput;
import springboot.model.Avaliacao;
import springboot.model.Usuario;
import springboot.service.AvaliacaoService;
import springboot.service.QuestaoService;

import java.io.IOException;

@Controller
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "+")
@Api(value = "AvaliacaoControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class AvaliacaoController {

    @Autowired
    AvaliacaoService avaliacaoService;

    @Autowired
    QuestaoService questaoService;

    @ApiOperation("Permite registrar uma nova avaliação no sistema. Requer que o corpo do request contenha um objeto com os campos: observacoes, questao e competencias.\r\n"
            + "")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Avaliacao.class) })
    @RequestMapping(value = "/avaliacao", method = RequestMethod.POST)
    public Avaliacao save(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody AvaliacaoInput avaliacao) throws IOException {
        return avaliacaoService.save(
                new Avaliacao(
                        avaliacao.getObservacoes(),
                        avaliacao.getCompetencias(),
                        usuario,
                        questaoService.getById(avaliacao.getQuestao())
                )
        );
    }

    @ApiOperation("Permite atualizar uma avaliação no sistema. Requer que o corpo do request contenha um objeto com os campos: observacoes, questao e competencias.\r\n"
            + "")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Avaliacao.class) })
    @RequestMapping(value = "/avaliacao/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Avaliacao> update(@PathVariable("id") String id, @RequestAttribute(name="usuario") Usuario usuario, @RequestBody AvaliacaoInput avaliacao) {

        Avaliacao updatedAvaliacao = avaliacaoService.update(
                new Avaliacao(
                        avaliacao.getObservacoes(),
                        avaliacao.getCompetencias(),
                        usuario,
                        questaoService.getById(avaliacao.getQuestao())
                ),
                id
        );
        return new ResponseEntity<Avaliacao>(updatedAvaliacao, HttpStatus.OK);
    }
}