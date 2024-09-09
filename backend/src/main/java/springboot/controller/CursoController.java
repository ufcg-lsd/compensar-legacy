package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import org.springframework.http.MediaType;
import springboot.dto.output.CursoOutput;
import springboot.dto.output.ModuloCursoOutput;
import springboot.enums.CourseType;
import springboot.enums.EstadoQuestao;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.*;
import springboot.model.ModuloCurso.EstadoModulo;
import springboot.repository.CompetenciaRepository;
import springboot.service.AvaliacaoService;
import springboot.service.CursoService;
import springboot.service.QuestaoService;
import springboot.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

@Controller
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
    private CompetenciaRepository competenciaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    QuestionSearchController questionSearchController;

    @Autowired
    private QuestaoService questaoService;

    @Autowired
    private AvaliacaoService avaliacaoService;

    private CursoService cursoService;

    @ApiOperation("Permite pegar a tela atual do usuário no curso de avaliação.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/", method = RequestMethod.GET)
    public ResponseEntity<CursoOutput> getCursoAvaliacoes(@RequestAttribute(name = "usuario") Usuario usuario) {
        return cursoService.auxCursoAvaliacoes(usuario, null);
    }

    /**
     * Cria e retorna novos cursos para o usuário logado, incluindo módulos de
     * avaliação e criação.
     * 
     * Este método verifica se o usuário possui cursos de avaliação e criação
     * configurados. Se não tiver, ele inicializa essas listas com módulos padrão e
     * os atualiza no banco de dados. Em seguida, retorna uma resposta contendo a
     * lista atualizada de módulos de avaliação e criação.
     * 
     * @param user O usuário para o qual os novos cursos serão criados. É
     *             esperado que o usuário esteja autenticado e que o atributo
     *             "usuario" esteja presente na requisição.
     * @return Um `ResponseEntity` contendo um `CursoOutput` com as listas de módulo
     *         de avaliação e criação atualizadas, e um status HTTP de sucesso (OK).
     */
    @ApiOperation("Cria novos cursos para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursos/", method = RequestMethod.GET)
    public ResponseEntity<CursoOutput> novosCursos(@RequestAttribute(name = "usuario") Usuario user) {
        try {
            if (cursoService.isCourseListEmpty(user.getCursoAvaliacao()))
                cursoService.initializeCourses(user, CourseType.AVALIACAO);

            if (cursoService.isCourseListEmpty(user.getCursoCriacao()))
            cursoService.initializeCourses(user, CourseType.CRIACAO);

            List<ModuloCursoOutput> cursoAvaliacao = cursoService.processModules(user.getCursoAvaliacao(), CourseType.AVALIACAO);
            List<ModuloCursoOutput> cursoCriacao = cursoService.processModules(user.getCursoCriacao(), CourseType.CRIACAO);

            user = usuarioService.update(user, user.getEmail());

            CursoOutput moduleStats = new CursoOutput(null, cursoAvaliacao, cursoCriacao);

            return new ResponseEntity<>(moduleStats, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: tratar melhor futuramente
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("Avança no curso de avaliação para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> avancarCursoAvaliacoes(@RequestAttribute(name = "usuario") Usuario usuario,
            @RequestBody List<Boolean> respostas) {

        if (usuario.getCursoAvaliacao() == null || usuario.getCursoAvaliacao().size() == 0) {
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);
        }

        String mensagem = null;

        for (int i = 0; i < usuario.getCursoAvaliacao().size(); i++) {
            ModuloCurso modulo = usuario.getCursoAvaliacao().get(i);
            if (!modulo.getEstado().equals(EstadoModulo.FINALIZADO)) {
                boolean sendToBack = false;
                Curso c = new Curso();
                if (i != 10)
                    c = competenciaRepository.findById(modulo.getNome()).get().getCursoAvaliacao();
                String competencia = modulo.getNome();
                if (modulo.getEstado().equals(EstadoModulo.DESCRICAO)) {
                    if (i == 0) {
                        modulo.setEstado(EstadoModulo.FINALIZADO);
                    } else {
                        modulo.setEstado(EstadoModulo.EXEMPLOS);
                    }
                } else if (modulo.getEstado().equals(EstadoModulo.EXEMPLOS)) {
                    modulo.setEstado(EstadoModulo.PRATICA);
                } else if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                    int total = 0;
                    int erros = 0;
                    if (i == usuario.getCursoAvaliacao().size() - 1) {
                        total = 9;
                        if (respostas == null || respostas.size() != 9) {
                            erros += 9;
                        } else {
                            int j = 0;
                            for (String comp : COMP_NAMES) {
                                if (!respostas.get(j)
                                        .equals(questaoService.evaluateQuestao(comp, modulo.getQuestoes().get(j))))
                                    erros++;
                                j++;
                            }
                        }
                    } else {
                        List<Boolean> l1 = questaoService.evaluateQuestoes(competencia, modulo.getQuestoes());
                        total = 2;
                        if (respostas == null || respostas.size() != 2 || !l1.equals(respostas)) {
                            erros++;
                        }
                    }
                    // Verifica se errou no máximo 30%
                    if (100 * erros <= total * 30) {
                        modulo.setEstado(EstadoModulo.FINALIZADO);
                        modulo.zeraErros();
                    } else if (i == usuario.getCursoAvaliacao().size() - 1) {
                        mensagem = "Você obteve menos de 70% de acerto, tente novamente ou volte e estude mais um pouco sobre as competências!";
                    } else {
                        modulo.addErro();
                        if (modulo.getErros() == 2) {
                            mensagem = "Percebemos que você novamente teve dificuldade em identificar a competência nas questões apresentadas, por isso deve voltar e ver mais um exemplo!";
                            modulo.setEstado(EstadoModulo.EXEMPLOS);
                        } else if (modulo.getErros() == 3) {
                            mensagem = "Como você não conseguiu identificar bem a presença ou ausência da competência nas questões apresentadas, veja uma nova competência e depois tentaremos essa novamente!";
                            sendToBack = true;
                            modulo.zeraErros();
                            modulo.setEstado(EstadoModulo.DESCRICAO);
                            // processa devido ao numero de erros
                        } else {
                            mensagem = "Você não conseguiu identificar a presença ou ausência da competência nas questões apresentadas, tente novamente!";
                        }
                    }

                }
                usuario.getCursoAvaliacao().set(i, modulo);
                if (sendToBack) {
                    usuario.getCursoAvaliacao().add(usuario.getCursoAvaliacao().get(10));
                    usuario.getCursoAvaliacao().remove(i);

                    usuario.getCursoAvaliacao().set(9, modulo);
                }
                usuario = usuarioService.update(usuario, usuario.getEmail());
                break;
            }
        }

        return cursoService.auxCursoAvaliacoes(usuario, mensagem);
    }

    @ApiOperation("Permite pegar a tela atual do usuário no curso de criação de questões.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoCriacao/", method = RequestMethod.GET)
    public ResponseEntity<CursoOutput> getCursoCriacao(@RequestAttribute(name = "usuario") Usuario usuario) {
        return cursoService.auxCursoCriacao(usuario, null);
    }

    @ApiOperation("Avança no curso de criação de questões para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoCriacao/", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> avancarCursoCriacao(@RequestAttribute(name = "usuario") Usuario usuario,
            @RequestBody List<String> respostas) {

        if (usuario.getCursoCriacao() == null || usuario.getCursoCriacao().size() == 0) {
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);
        }

        String mensagem = null;

        for (int i = 0; i < usuario.getCursoCriacao().size(); i++) {
            ModuloCurso modulo = usuario.getCursoCriacao().get(i);
            if (!modulo.getEstado().equals(EstadoModulo.FINALIZADO)) {
                boolean sendToBack = false;
                Curso c = new Curso();
                if (i != 10)
                    c = competenciaRepository.findById(modulo.getNome()).get().getCursoCriacao();
                String competencia = modulo.getNome();
                if (modulo.getEstado().equals(EstadoModulo.DESCRICAO)) {
                    if (i == 0) {
                        modulo.setEstado(EstadoModulo.FINALIZADO);
                    } else {
                        modulo.setEstado(EstadoModulo.EXEMPLOS);
                    }
                } else if (modulo.getEstado().equals(EstadoModulo.EXEMPLOS)) {
                    modulo.setEstado(EstadoModulo.PRATICA);
                } else if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                    if (i != 10) {
                        try {
                            Boolean hasCompetencia = true;
                            // Boolean hasCompetencia = questaoService.hasCompetencia(competencia,
                            //         questaoService.getSetCompetencias(respostas.get(0)));
                            if (!hasCompetencia) {
                                modulo.addErro();
                                if (modulo.getErros() == 2) {
                                    mensagem = "Percebemos que você teve dificuldade com essa competência, então volte e veja algum exemplo";
                                    modulo.setEstado(EstadoModulo.EXEMPLOS);
                                } else if (modulo.getErros() == 3) {
                                    mensagem = "Como você não conseguiu identificar bem a competência, veja uma nova competência e depois voltamos para essa";
                                    sendToBack = true;
                                    modulo.zeraErros();
                                    modulo.setEstado(EstadoModulo.DESCRICAO);
                                    // processa devido ao numero de erros
                                } else {
                                    mensagem = "Não conseguimos identificar a presença da competência atual no enunciado, tente novamente!";
                                }
                            } else {
                                modulo.setEstado(EstadoModulo.FINALIZADO);
                                modulo.zeraErros();
                            }
                        } catch (Exception e) {
                            mensagem = "Problema interno ao classificar questão, tente novamente!";
                        }
                    } else {
                        try {
                            Questao q;
                            String questao = modulo.getQuestoes().get(0);
                            q = questaoService.getById(questao);
                            if (!q.getAutor().equals(usuario.getEmail())) {
                                mensagem = "A questão precisa ser de sua autoria!";
                            } else if (q.getEstado() != EstadoQuestao.PUBLICADA) {
                                mensagem = "Você deve criar uma nova questão para prosseguir!";
                            } else {
                                Avaliacao avaliacaoAutor = null;
                                List<Avaliacao> avaliacoes = avaliacaoService.getAllByQuestao(questao);
                                for (Avaliacao a : avaliacoes) {
                                    if (a.getAutor().equals(usuario.getEmail())) {
                                        avaliacaoAutor = a;
                                        break;
                                    }
                                }

                                if (avaliacaoAutor == null) {
                                    mensagem = "Sua questão não foi avaliada apropriadamente!";
                                } else if (!avaliacaoAutor.getCompetencias().equals(q.getCompetencias())) {
                                    mensagem = "Você deve criar uma nova questão para prosseguir!";
                                } else {
                                    modulo.setEstado(EstadoModulo.FINALIZADO);
                                }
                            }
                        } catch (Exception e) {
                            mensagem = "Você deve criar uma nova questão para prosseguir!";
                        }
                    }
                }
                usuario.getCursoCriacao().set(i, modulo);
                if (sendToBack) {
                    usuario.getCursoCriacao().add(usuario.getCursoCriacao().get(10));
                    usuario.getCursoCriacao().remove(i);

                    usuario.getCursoCriacao().set(9, modulo);
                }
                usuario = usuarioService.update(usuario, usuario.getEmail());
                break;
            }
        }

        return cursoService.auxCursoCriacao(usuario, mensagem);
    }

    @ApiOperation("Registra questão criada na avaliação final do usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoCriacao/newQuestion", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> registraCriacaoQuestao(@RequestAttribute(name = "usuario") Usuario usuario,
            @RequestBody String questao) {

        if (usuario.getCursoCriacao() == null || usuario.getCursoCriacao().size() == 0) {
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);
        }

        List<String> modulo = new ArrayList<>();
        modulo.add(questao);

        String mensagem = null;

        try {
            questaoService.getById(questao);
            usuario.getCursoCriacao().get(10).setQuestoes(modulo);
            usuario = usuarioService.update(usuario, usuario.getEmail());
        } catch (RegisterNotFoundException e) {
            mensagem = "A questão especificada não existe";
        }

        return cursoService.auxCursoCriacao(usuario, mensagem);
    }

    public class NewQuestion {
        private int competencia;
        private String questao;

        public int getCompetencia() {
            return competencia;
        }

        public void setCompetencia(int competencia) {
            this.competencia = competencia;
        }

        public String getQuestao() {
            return questao;
        }

        public void setQuestao(String questao) {
            this.questao = questao;
        }
    }
}
