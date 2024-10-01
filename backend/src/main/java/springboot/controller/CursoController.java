package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import springboot.dto.output.CursoOutput;
import springboot.dto.output.ModuloCursoOutput;
import springboot.enums.CourseType;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.*;
import springboot.model.ModuloCurso.EstadoModulo;
import springboot.service.CursoService;
import springboot.service.QuestaoService;
import springboot.service.UsuarioService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
@Tag(name = "CursoController", description = "API para gerenciar cursos")
public class CursoController {
    public static final String COMP_NAMES[] = new String[] {
            "COMP_ABSTRAÇÃO",
            "COMP_ALGORITMOS",
            "COMP_ANÁLISE",
            "COMP_AUTOMAÇÃO",
            "COMP_COLETA",
            "COMP_DECOMPOSIÇÃO",
            "COMP_PARALELIZAÇÃO",
            "COMP_REPRESENTAÇÃO",
            "COMP_SIMULAÇÃO"
    };

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private QuestaoService questaoService;

    @Autowired
    private CursoService cursoService;

    /**
     * Retorna a tela atual do usuário no curso de avaliação.
     *
     * @param user Usuário logado no sistema, injetado pelo framework.
     * @return ResponseEntity contendo o estado atual do curso de avaliação.
     */
    @Operation(summary = "Retorna a tela atual do usuário no curso de avaliação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoOutput.class)))
    })
    @GetMapping(value = "/cursoAvaliacao")
    public ResponseEntity<CursoOutput> getReviewCourse(@RequestAttribute(name = "usuario") Usuario user) {
        return cursoService.auxCursoAvaliacoes(user, null);
    }

    /**
     * Cria novos cursos para o usuário logado.
     *
     * Este método verifica se o usuário possui cursos de avaliação e criação
     * configurados. Se não tiver, inicializa estas listas com módulos padrão e
     * os atualiza no banco de dados.
     *
     * @param user Usuário autenticado, para o qual os cursos serão criados.
     * @return Um ResponseEntity contendo o CursoOutput atualizado e status HTTP de
     *         sucesso.
     */
    @Operation(summary = "Cria novos cursos para o usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoOutput.class)))
    })
    @GetMapping(value = "/cursos")
    public ResponseEntity<CursoOutput> newCourses(@RequestAttribute(name = "usuario") Usuario user) {
        try {
            if (cursoService.isCourseListEmpty(user.getCursoAvaliacao()))
                cursoService.initializeCourses(user, CourseType.AVALIACAO);

            if (cursoService.isCourseListEmpty(user.getCursoCriacao()))
                cursoService.initializeCourses(user, CourseType.CRIACAO);

            List<ModuloCursoOutput> cursoAvaliacao = cursoService.processModules(user.getCursoAvaliacao(),
                    CourseType.AVALIACAO, user);
            List<ModuloCursoOutput> cursoCriacao = cursoService.processModules(user.getCursoCriacao(),
                    CourseType.CRIACAO, user);

            usuarioService.update(user, user.getEmail());

            CursoOutput moduleStats = new CursoOutput(null, cursoAvaliacao, cursoCriacao);

            return new ResponseEntity<>(moduleStats, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: tratar melhor futuramente
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Avança no curso de avaliação para o usuário logado.
     *
     * @param user    Usuário logado no sistema.
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @return ResponseEntity contendo o resultado da operação.
     */
    @Operation(summary = "Avança no curso de avaliação para o usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoOutput.class)))
    })
    @PostMapping(value = "/cursoAvaliacao")
    public ResponseEntity<CursoOutput> advanceReviewCourse(@RequestAttribute(name = "usuario") Usuario user,
            @RequestBody List<Boolean> answers) {

        if (cursoService.isCourseListEmpty(user.getCursoAvaliacao())) {
            return new ResponseEntity<>(new CursoOutput(), HttpStatus.NOT_FOUND);
        }

        String message = null;

        for (int i = 0; i < user.getCursoAvaliacao().size(); i++) {
            ModuloCurso module = user.getCursoAvaliacao().get(i);

            if (!module.getEstado().equals(EstadoModulo.FINALIZADO)) {
                message = cursoService.processEvaluationModule(user, answers, i, module);
                break;
            }
        }

        return cursoService.auxCursoAvaliacoes(user, message);
    }

    /**
     * Obtém a tela atual do usuário no curso de criação de questões.
     *
     * Este endpoint permite que o usuário logado obtenha a tela atual do
     * curso de criação de questões, retornando um estado atual do curso.
     *
     * @param user Usuário logado no sistema.
     * @return ResponseEntity contendo o estado atual do curso de criação
     *         ou um erro caso ocorra alguma falha.
     */
    @Operation(summary = "Obtém a tela atual do usuário no curso de criação de questões.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoOutput.class))),
            @ApiResponse(responseCode = "404", description = "Curso de criação de questões não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoOutput.class)))
    })
    @GetMapping("/cursoCriacao")
    public ResponseEntity<CursoOutput> getCreationCourse(@RequestAttribute(name = "usuario") Usuario user) {
        return cursoService.auxCursoCriacao(user, null);
    }

    /**
     * Avança o curso de criação de questões para o usuário logado.
     *
     * Este método percorre os módulos do curso de criação do usuário, processando
     * o primeiro módulo cujo estado não seja "FINALIZADO". Utiliza o serviço de
     * curso para verificar a validade da lista de cursos e avançar o estado do
     * módulo.
     *
     * @param user    Usuário logado no sistema, injetado pelo framework.
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @return ResponseEntity com o resultado do avanço no curso de criação.
     */
    @Operation(summary = "Avança no curso de criação de questões para o usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoOutput.class)))
    })
    @PostMapping(value = "/cursoCriacao")
    public ResponseEntity<CursoOutput> advanceCreationCourse(@RequestAttribute(name = "usuario") Usuario user,
            @RequestBody List<String> answers) {

        if (cursoService.isCourseListEmpty(user.getCursoCriacao())) {
            return new ResponseEntity<>(new CursoOutput(), HttpStatus.NOT_FOUND);
        }

        String message = null;

        for (int i = 0; i < user.getCursoCriacao().size(); i++) {
            ModuloCurso module = user.getCursoCriacao().get(i);

            if (!module.getEstado().equals(EstadoModulo.FINALIZADO)) {
                message = cursoService.processModuleCreation(user, answers, i, module);
                break;
            }
        }

        return cursoService.auxCursoCriacao(user, message);
    }

    /**
     * Registra uma nova questão criada pelo usuário logado no curso de criação.
     *
     * Este método verifica se a questão fornecida existe e, se existir, adiciona a
     * questão ao curso de criação do usuário. Caso contrário, retorna uma mensagem
     * de erro indicando que a questão especificada não foi encontrada.
     *
     * @param user     Usuário logado no sistema, injetado pelo framework.
     * @param question ID da questão a ser registrada.
     * @return ResponseEntity com o resultado do registro da questão no curso de
     *         criação.
     */
    @Operation(summary = "Registra uma nova questão criada na avaliação final do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoOutput.class))),
            @ApiResponse(responseCode = "404", description = "Questão não encontrada", content = @Content)
    })
    @PostMapping(value = "/cursoCriacao/newQuestion")
    public ResponseEntity<CursoOutput> registraCriacaoQuestao(
            @RequestAttribute(name = "usuario") Usuario user,
            @RequestBody String question) {

        if (cursoService.isCourseListEmpty(user.getCursoCriacao())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CursoOutput());
        }

        String message = null;

        try {
            questaoService.getById(question);
            user.getCursoCriacao().get(10).setQuestoes(Collections.singletonList(question));
            user = usuarioService.update(user, user.getEmail());

        } catch (RegisterNotFoundException e) {
            message = "A questão especificada não existe.";
        }

        return cursoService.auxCursoCriacao(user, message);
    }
}
