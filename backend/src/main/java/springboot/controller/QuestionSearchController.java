package springboot.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import springboot.dto.IO.QuestaoIO;
import springboot.dto.output.QuestaoOutput;
import springboot.enums.EstadoQuestao;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.QuestaoService;

@RestController
@RequestMapping(value = "/api/questao")
@CrossOrigin(origins = "*")
public class QuestionSearchController {

    @Autowired
    private QuestaoIO questaoIO;

    @Autowired
    private QuestaoService questaoService;
    
    private QuestaoOutput convertToOutput(Questao questao, Usuario usuario) {
        return questaoIO.toDto(questao, usuario, false);
    }

    @Operation(summary = "Fornece um array de objetos do tipo questão registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @GetMapping("/{page}/{size}")
    public Page<QuestaoOutput> getAll(
        @RequestAttribute(name="usuario") Usuario usuario, 
        @PathVariable int page, 
        @PathVariable int size
    ) {
        return questaoService.getAll(page, size)
                             .map(q -> convertToOutput(q, usuario));
    }

    @Operation(summary = "Fornece os dados de uma questão em particular.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @GetMapping("/{id}")
    public QuestaoOutput getById(
        @RequestAttribute(name="usuario") Usuario usuario, 
        @PathVariable String id
    ) {
        return convertToOutput(questaoService.getById(id), usuario);
    }

    @Operation(summary = "Busca questões com base em diversos critérios.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @GetMapping("/search")
    public Page<QuestaoOutput> search(
        @RequestAttribute(name="usuario") Usuario usuario,
        @RequestParam(required = false) String enunciado,
        @RequestParam(required = false) Set<String> competencias,
        @RequestParam(required = false) String autor,
        @RequestParam(required = false) String fonte,
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) Set<String> conteudo,
        @RequestParam int page,
        @RequestParam int size
    ) {
        Set<EstadoQuestao> estados = new HashSet<>();
        estados.add(EstadoQuestao.PUBLICADA);
        Set<String> newConteudo = cleanSet(conteudo);
        return questaoService.getByEnunciadoCompetenciasAutorFonteTipo(
            enunciado, (HashSet<String>) competencias, autor, "", fonte, tipo, newConteudo, estados, page, size
        ).map(q -> convertToOutput(q, usuario));
    }

    @Operation(summary = "Busca questões do usuário logado com base em diversos critérios.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @GetMapping("/searchMy")
    public Page<QuestaoOutput> searchMyQuestions(
        @RequestAttribute(name="usuario") Usuario usuario,
        @RequestParam(required = false) String enunciado,
        @RequestParam(required = false) Set<String> competencias,
        @RequestParam(required = false) Set<EstadoQuestao> estados,
        @RequestParam(required = false) String fonte,
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) Set<String> conteudo,
        @RequestParam int page,
        @RequestParam int size
    ) {
        Set<String> newConteudo = cleanSet(conteudo);
        return questaoService.getByEnunciadoCompetenciasAutorFonteTipo(
            enunciado, (HashSet<String>) competencias, "", usuario.getEmail(), fonte, tipo, newConteudo, estados, page, size
        ).map(q -> convertToOutput(q, usuario));
    }

    private Set<String> cleanSet(Set<String> originalSet) {
        Set<String> cleanedSet = new HashSet<>();
        if (originalSet != null) {
            for (String item : originalSet) {
                if (item != null && !item.isEmpty()) {
                    cleanedSet.add(item);
                }
            }
        }
        return cleanedSet;
    }
}
