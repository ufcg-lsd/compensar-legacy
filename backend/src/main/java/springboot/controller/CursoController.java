package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import org.springframework.http.MediaType;
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
@CrossOrigin(origins = "+")
@Api(value = "UsuariosControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)

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
    @ApiOperation("Permite pegar a tela atual do usuário no curso de avaliação.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/", method = RequestMethod.GET)
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
    @ApiOperation("Cria novos cursos para o usuário logado.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @GetMapping(value = "/cursos/")
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

            user = usuarioService.update(user, user.getEmail());

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
    @ApiOperation("Avança no curso de avaliação para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> advanceReviewCourse(@RequestAttribute(name = "usuario") Usuario user,
            @RequestBody List<Boolean> answers) {

        if (cursoService.isCourseListEmpty(user.getCursoAvaliacao()))
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);

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
    @ApiOperation("Obtém a tela atual do usuário no curso de criação de questões.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CursoOutput.class),
            @ApiResponse(code = 404, message = "Curso de criação de questões não encontrado", response = CursoOutput.class)
    })
    @GetMapping("/cursoCriacao/")
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
    @ApiOperation("Avança no curso de criação de questões para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoCriacao/", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> advanceCreationCourse(@RequestAttribute(name = "usuario") Usuario user,
            @RequestBody List<String> answers) {

        if (cursoService.isCourseListEmpty(user.getCursoCriacao()))
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);

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
    @ApiOperation("Registra uma nova questão criada na avaliação final do usuário logado.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoCriacao/newQuestion", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> registraCriacaoQuestao(
            @RequestAttribute(name = "usuario") Usuario user,
            @RequestBody String question) {

        // Verifica se o curso de criação do usuário está presente e não está vazio.
        if (cursoService.isCourseListEmpty(user.getCursoCriacao())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CursoOutput());
        }

        String message = null;

        try {
            // Verifica se a questão existe no sistema.
            questaoService.getById(question);

            // Adiciona a questão ao módulo apropriado no curso de criação do usuário.
            user.getCursoCriacao().get(10).setQuestoes(Collections.singletonList(question));
            user = usuarioService.update(user, user.getEmail());

        } catch (RegisterNotFoundException e) {
            // Define mensagem de erro caso a questão não seja encontrada.
            message = "A questão especificada não existe.";
        }

        // Retorna o resultado do processamento do curso de criação.
        return cursoService.auxCursoCriacao(user, message);
    }

}
