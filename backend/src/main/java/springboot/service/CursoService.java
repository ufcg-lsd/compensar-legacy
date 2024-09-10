package springboot.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springboot.controller.QuestionSearchController;
import springboot.dto.output.CursoOutput;
import springboot.dto.output.ModuloCursoOutput;
import springboot.enums.CourseType;
import springboot.enums.EstadoQuestao;
import springboot.model.*;
import springboot.model.ModuloCurso.EstadoModulo;
import springboot.repository.CompetenciaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {
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
    private AvaliacaoService avaliacaoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private QuestaoService questaoService;

    private QuestionSearchController questionSearchController;

    /**
     * Processa a lista de módulos e gera uma lista de objetos de saída
     * correspondentes.
     * 
     * @param modules    Lista de módulos a ser processada.
     * @param courseType
     * @param user
     * @return Lista de objetos `ModuloCursoOutput` gerados a partir dos módulos.
     */
    public List<ModuloCursoOutput> processModules(List<ModuloCurso> modules, CourseType courseType, Usuario user) {
        List<ModuloCursoOutput> outputList = new ArrayList<>();
        boolean active = false;

        for (int i = 0; i < modules.size() - 1; i++) {
            ModuloCurso module = modules.get(i);

            @SuppressWarnings("unlikely-arg-type")
            Curso course = courseType.equals("AVALIACAO")
                    ? competenciaRepository.findById(module.getNome()).get().getCursoAvaliacao()
                    : competenciaRepository.findById(module.getNome()).get().getCursoCriacao();
            ModuloCursoOutput moduleOutput = createCourseModuleOutput(module, course, courseType);

            if (active)
                moduleOutput.setEstado(EstadoModulo.INATIVO);

            if (moduleOutput.getEstado() != EstadoModulo.FINALIZADO)
                active = true;

            modules.set(i, module);
            outputList.add(moduleOutput);
        }

        // Processar o último módulo
        ModuloCurso evaluationMod = modules.get(10);
        ModuloCursoOutput evaluationOutput = processFinalModule(evaluationMod, courseType, user);

        if (active)
            evaluationOutput.setEstado(EstadoModulo.INATIVO);

        modules.set(10, evaluationMod);
        outputList.add(evaluationOutput);

        return outputList;
    }

    /**
     * Processa o módulo final do curso e adiciona detalhes das questões se
     * disponível.
     * 
     * @param courseType
     * @param user
     * @param evaluationMod O módulo final de avaliação.
     * @return Um objeto `ModuloCursoOutput` com as informações do módulo final.
     */
    private ModuloCursoOutput processFinalModule(ModuloCurso evaluationMod, CourseType type, Usuario user) {
        ModuloCursoOutput evaluationOutput = new ModuloCursoOutput();
        evaluationOutput.setNome(evaluationMod.getNome());
        evaluationOutput.setEstado(evaluationMod.getEstado());

        if (evaluationMod.getEstado().equals(EstadoModulo.PRATICA)) {
            if (type.equals("CRIACAO")) {
                if (evaluationMod.getQuestoes().size() > 0) {

                    try {
                        String question = evaluationMod.getQuestoes().get(0);
                        evaluationOutput.getQuestoesDetalhadas().add(questionSearchController.getById(user,
                                question));

                        evaluationOutput.getQuestoes().add(question);

                    } catch (Exception e) {
                        user.getCursoCriacao().get(10).getQuestoes().clear();
                    }
                }
            } else {
                evaluationMod.setQuestoes(new ArrayList<>());

                for (String comp : COMP_NAMES) {
                    Questao sample = questaoService.getSample(comp);
                    evaluationMod.getQuestoes().add(sample.getId());
                    evaluationOutput.getQuestoes().add(sample.getEnunciado());
                }
            }
        }

        return evaluationOutput;
    }

    /**
     * Cria um objeto `ModuloCursoOutput` a partir de um módulo de curso e seu
     * respectivo `Curso`.
     * 
     * @param module     O módulo de curso a ser convertido.
     * @param course     O curso associado ao módulo.
     * @param courseType O tipo do curso (AVALIACAO ou CRIACAO).
     * @return Um objeto `ModuloCursoOutput` com as informações do módulo.
     */
    private ModuloCursoOutput createCourseModuleOutput(ModuloCurso module, Curso course, CourseType courseType) {
        ModuloCursoOutput output = new ModuloCursoOutput();
        output.setNome(module.getNome());
        output.setDescricao(course.getDescricao());
        output.setEstado(module.getEstado());
        output.setVideo(course.getVideo());

        switch (module.getEstado()) {
            case EXEMPLOS:
                updateExamples(module, course, output);
                break;
            case PRATICA:
                addPracticeDetails(module, course, output, courseType);
                break;
            case FINALIZADO:
                addExamplesAndTexts(module, course, output);
                break;
            default:
                break;
        }

        return output;
    }

    /**
     * Atualiza os exemplos do módulo de curso no estado EXEMPLOS.
     * 
     * @param module O módulo de curso a ser atualizado.
     * @param course O curso associado ao módulo.
     * @param output O objeto de saída a ser atualizado.
     */
    private void updateExamples(ModuloCurso module, Curso course, ModuloCursoOutput output) {
        int index = (module.getNumeroExemplo() + 1) % course.getExemplos().size();
        module.setNumeroExemplo(index);
        output.getExemplos().add(course.getExemplos().get(index));
        output.getTextoExemplos().add(course.getTextoExemplos().get(index));
    }

    /**
     * Adiciona exemplos e textos ao objeto `ModuloCursoOutput` com base no módulo.
     * 
     * @param module O módulo de curso que contém os exemplos e textos.
     * @param course O curso associado ao módulo.
     * @param output O objeto `ModuloCursoOutput` onde os exemplos e textos serão
     *               adicionados.
     */
    private void addExamplesAndTexts(ModuloCurso module, Curso course, ModuloCursoOutput output) {
        output.setExemplos(course.getExemplos());
        output.setTextoExemplos(course.getTextoExemplos());
    }

    /**
     * Adiciona detalhes da prática ao módulo de curso no estado PRATICA.
     * 
     * @param module     O módulo de curso a ser atualizado.
     * @param course     O curso associado ao módulo.
     * @param output     O objeto de saída a ser atualizado.
     * @param courseType O tipo de curso acessado.
     */
    @SuppressWarnings("unlikely-arg-type")
    private void addPracticeDetails(ModuloCurso module, Curso course, ModuloCursoOutput output, CourseType courseType) {
        int index = module.getNumeroExemplo();
        output.getExemplos().add(course.getExemplos().get(index));
        output.getTextoExemplos().add(course.getTextoExemplos().get(index));

        if (courseType.equals("AVALIACAO")) {
            List<Questao> samples = questaoService.getSamples(module.getNome());

            for (Questao q : samples) {
                module.getQuestoes().add(q.getId());
                output.getQuestoes().add(q.getEnunciado());
            }
        }
    }

    /**
     * Processa e retorna a lista de módulos de avaliação do curso para um usuário.
     * 
     * @param user    O usuário cujos módulos de avaliação serão processados.
     * @param message Mensagem adicional a ser incluída na resposta.
     * @return Um `ResponseEntity` contendo um `CursoOutput` com a lista de módulos
     *         de avaliação processados e um status HTTP.
     */
    public ResponseEntity<CursoOutput> auxCursoAvaliacoes(@RequestAttribute(name = "usuario") Usuario user,
            String message) {
        if (isCourseListEmpty(user.getCursoAvaliacao()))
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);

        List<ModuloCursoOutput> outputList = processModules(user.getCursoAvaliacao(), CourseType.AVALIACAO, user);

        user = usuarioService.update(user, user.getEmail());

        return new ResponseEntity<>(new CursoOutput(message, outputList, null), HttpStatus.OK);
    }

    /**
     * Verifica se a lista de cursos está vazia ou é nula.
     * 
     * @param courses Lista de módulos de curso a ser verificada.
     * @return `true` se a lista estiver vazia ou nula, `false` caso contrário.
     */
    public boolean isCourseListEmpty(List<ModuloCurso> courses) {
        return courses == null || courses.isEmpty();
    }

    /**
     * Inicializa a lista de módulos de curso para um usuário, com base no tipo de
     * curso especificado.
     * 
     * Este método cria uma lista de módulos padrão para o usuário, dependendo do
     * tipo de curso (avaliação ou criação). Os módulos incluem uma introdução,
     * competências padrão e uma avaliação final. O estado do módulo de avaliação
     * final é definido como `PRATICA`.
     * 
     * @param user O usuário cujos cursos serão inicializados.
     * @param type O tipo de curso a ser inicializado. Pode ser
     *             `UsuarioCursoType.AVALIACAO` para módulos de avaliação ou
     *             `UsuarioCursoType.CRIACAO` para módulos de criação.
     * @return Uma lista de módulos de curso inicializada com os módulos padrão.
     */
    public void initializeCourses(Usuario user, CourseType type) {
        List<ModuloCurso> courses = new ArrayList<>();
        courses.add(new ModuloCurso("COMP_INTRODUÇÃO"));

        for (String competence : COMP_NAMES)
            courses.add(new ModuloCurso(competence));

        ModuloCurso finalModule = new ModuloCurso("COMP_AVALIAÇÃO FINAL");
        finalModule.setEstado(EstadoModulo.PRATICA);
        courses.add(finalModule);

        if (type == CourseType.AVALIACAO)
            user.setCursoAvaliacao(courses);
        else if (type == CourseType.CRIACAO)
            user.setCursoCriacao(courses);

        usuarioService.update(user, user.getEmail());
    }

    /**
     * Processa e retorna a lista de módulos de criação do curso para um usuário.
     * 
     * @param user    O usuário cujos módulos de avaliação serão processados.
     * @param message Mensagem adicional a ser incluída na resposta.
     * @return Um `ResponseEntity` contendo um `CursoOutput` com a lista de módulos
     *         de criação processados e um status HTTP.
     */
    public ResponseEntity<CursoOutput> auxCursoCriacao(@RequestAttribute(name = "usuario") Usuario user,
            String message) {
        if (isCourseListEmpty(user.getCursoCriacao()))
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);

        List<ModuloCursoOutput> outputList = processModules(user.getCursoCriacao(), CourseType.CRIACAO, user);

        user = usuarioService.update(user, user.getEmail());

        return new ResponseEntity<CursoOutput>(new CursoOutput(message, null, outputList), HttpStatus.OK);
    }

    /**
     * Processa o módulo de avaliação do usuário.
     *
     * @param user    Usuário logado no sistema.
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @param index   Índice do módulo atual.
     * @param module  Módulo do curso a ser processado.
     * @return Mensagem de feedback para o usuário.
     */
    public String processEvaluationModule(Usuario user, List<Boolean> answers, int index, ModuloCurso module) {
        String message = null;
        boolean sendToBack = false;

        switch (module.getEstado()) {
            case DESCRICAO:
                processStateDescription(module, index);
                break;
            case EXEMPLOS:
                module.setEstado(EstadoModulo.PRATICA);
                break;
            case PRATICA:
                message = processStatePractice(user, answers, index, module);
                sendToBack = checkNeedReturn(module);
                break;
            default:
                break;
        }

        user.getCursoAvaliacao().set(index, module);

        if (sendToBack) {
            reorderModules(user.getCursoAvaliacao(), module, index);
        }

        usuarioService.update(user, user.getEmail());
        return message;
    }

    /**
     * Processa o módulo de criação do usuário.
     *
     * @param user    Usuário logado no sistema.
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @param index   Índice do módulo atual.
     * @param module  Módulo do curso a ser processado.
     * @return Mensagem de feedback para o usuário.
     */
    public String processModuleCreation(Usuario user, List<String> answers, int index, ModuloCurso module) {
        boolean sendToBack = false;
        String message = null;

        switch (module.getEstado()) {
            case DESCRICAO:
                processStateDescription(module, index);
                break;
            case EXEMPLOS:
                module.setEstado(EstadoModulo.PRATICA);
                break;
            case PRATICA:
                message = processStatePractice(user, answers, module, index);
                sendToBack = checkNeedReturn(module);
                break;
            default:
                break;
        }

        user.getCursoCriacao().set(index, module);

        if (sendToBack) {
            reorderModules(user.getCursoCriacao(), module, index);
        }

        usuarioService.update(user, user.getEmail());
        return message;
    }

    /**
     * Processa o estado "DESCRICAO" do módulo.
     *
     * @param module Módulo do curso.
     * @param index  Índice do módulo atual.
     */
    private void processStateDescription(ModuloCurso module, int index) {
        if (index == 0) {
            module.setEstado(EstadoModulo.FINALIZADO);
        } else {
            module.setEstado(EstadoModulo.EXEMPLOS);
        }
    }

    /**
     * Processa o estado "PRATICA" do módulo.
     *
     * @param user    Usuário logado no sistema.
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @param index   Índice do módulo atual.
     * @param module  Módulo do curso.
     * @return Mensagem de feedback para o usuário.
     */
    private String processStatePractice(Usuario user, List<Boolean> answers, int index, ModuloCurso module) {
        String message = null;
        int total = 0;
        int errors = 0;

        if (index == user.getCursoAvaliacao().size() - 1) {
            total = 9;
            errors = evaluateFinalAnswers(answers, module, total);
        } else {
            total = 2;
            errors = evaluateIntermediateResponses(answers, module, total);
        }

        message = checkForErrors(user, module, total, errors, index);
        return message;
    }

    /**
     * Processa o estado "PRATICA" do módulo.
     *
     * @param user    Usuário logado no sistema.
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @param module  Módulo do curso.
     * @param index   Índice do módulo atual.
     * @return Mensagem de feedback para o usuário.
     */
    private String processStatePractice(Usuario user, List<String> answers, ModuloCurso module, int index) {
        String message = null;

        if (index != 10) {
            message = evaluateCompetence(user, answers, module);
        } else {
            message = evaluateFinalQuestion(user, module);
        }

        return message;
    }

    /**
     * Avalia as respostas finais do usuário.
     *
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @param module  Módulo do curso.
     * @param total   Total de questões avaliadas.
     * @return Quantidade de erros cometidos pelo usuário.
     */
    private int evaluateFinalAnswers(List<Boolean> answers, ModuloCurso module, int total) {
        int errors = 0;
        if (answers == null || answers.size() != total) {
            return total; // Erro total
        }
        for (int j = 0; j < total; j++) {
            if (!answers.get(j).equals(questaoService.evaluateQuestao(COMP_NAMES[j], module.getQuestoes().get(j)))) {
                errors++;
            }
        }
        return errors;
    }

    /**
     * Avalia as respostas intermediárias do usuário.
     *
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @param module  Módulo do curso.
     * @param total   Total de questões avaliadas.
     * @return Quantidade de erros cometidos pelo usuário.
     */
    private int evaluateIntermediateResponses(List<Boolean> answers, ModuloCurso module, int total) {
        int errors = 0;
        List<Boolean> respostasEsperadas = questaoService.evaluateQuestoes(module.getNome(), module.getQuestoes());
        if (answers == null || answers.size() != total || !respostasEsperadas.equals(answers)) {
            errors++;
        }
        return errors;
    }

    /**
     * Verifica se o número de erros permite o avanço ou se o usuário precisa
     * revisar.
     *
     * @param user   Usuário logado no sistema.
     * @param module Módulo do curso.
     * @param total  Total de questões avaliadas.
     * @param errors Número de erros cometidos.
     * @param index  Índice do módulo atual.
     * @return Mensagem de feedback para o usuário.
     */
    private String checkForErrors(Usuario user, ModuloCurso module, int total, int errors, int index) {
        String message = null;

        if (100 * errors <= total * 30) { // Erros <= 30%
            module.setEstado(EstadoModulo.FINALIZADO);
            module.zeraErros();
        } else if (index == user.getCursoAvaliacao().size() - 1) {
            message = "Você obteve menos de 70% de acerto, tente novamente ou volte e estude mais um pouco sobre as competências!";
        } else {
            message = handleErrorModule(module, "Modulo");
        }

        return message;
    }

    /**
     * Lida com os erros do módulo e define o estado adequado.
     *
     * @param module  Módulo do curso.
     * @param context Contexto de erro (ex.: "Modulo" ou "Competencia").
     * @return Mensagem de feedback para o usuário.
     */
    private String handleErrorModule(ModuloCurso module, String context) {
        String message;
        module.addErro();

        if (module.getErros() == 2) {
            message = String.format(
                    "Percebemos que você teve dificuldade %s, por isso deve voltar e ver mais um exemplo!",
                    context.equals("Competencia") ? "com essa competência"
                            : "em identificar a competência nas questões apresentadas");
            module.setEstado(EstadoModulo.EXEMPLOS);
        } else if (module.getErros() == 3) {
            message = String.format(
                    "Como você não conseguiu identificar bem %s, veja uma nova competência e depois tentaremos essa novamente!",
                    context.equals("Competencia") ? "a competência"
                            : "a presença ou ausência da competência nas questões apresentadas");
            module.zeraErros();
            module.setEstado(EstadoModulo.DESCRICAO);
        } else {
            message = String.format("%s, tente novamente!",
                    context.equals("Competencia")
                            ? "Não conseguimos identificar a presença da competência atual no enunciado a presença da competência atual no enunciado"
                            : "Você não conseguiu identificar a presença ou ausência da competência nas questões apresentadas");
        }

        return message;
    }

    /**
     * Verifica se o módulo deve ser enviado para o final da lista de avaliação.
     *
     * @param module Módulo do curso.
     * @return True se o módulo deve ser enviado para o final, False caso contrário.
     */
    private boolean checkNeedReturn(ModuloCurso module) {
        return module.getErros() == 3;
    }

    /**
     * Reordena os módulos de avaliação do usuário, enviando o módulo atual para o
     * final.
     *
     * @param course O curso associado ao módulo.
     * @param module Módulo do curso.
     * @param index  Índice do módulo atual.
     */
    private void reorderModules(List<ModuloCurso> course, ModuloCurso module, int index) {
        course.add(course.get(10));
        course.remove(index);
        course.set(9, module);
    }

    /**
     * Valida a avaliação da questão.
     *
     * @param user     Usuário logado no sistema.
     * @param question Questão a ser avaliada.
     * @param module   Módulo do curso.
     * @return Mensagem de feedback para o usuário.
     */
    private String validateEvaluationQuestion(Usuario user, Questao question, ModuloCurso module) {
        String message = null;
        Avaliacao authorReview = null;

        List<Avaliacao> reviews = avaliacaoService.getAllByQuestao(question.getId());
        for (Avaliacao a : reviews) {
            if (a.getAutor().equals(user.getEmail())) {
                authorReview = a;
                break;
            }
        }

        if (authorReview == null) {
            message = "Sua questão não foi avaliada apropriadamente!";
        } else if (!authorReview.getCompetencias().equals(question.getCompetencias())) {
            message = "Você deve criar uma nova questão para prosseguir!";
        } else {
            module.setEstado(EstadoModulo.FINALIZADO);
        }

        return message;
    }

    /**
     * Avalia a questão final do usuário.
     *
     * @param user   Usuário logado no sistema.
     * @param module Módulo do curso.
     * @return Mensagem de feedback para o usuário.
     */
    private String evaluateFinalQuestion(Usuario user, ModuloCurso module) {
        String message = null;

        try {
            Questao question = questaoService.getById(module.getQuestoes().get(0));
            if (!question.getAutor().equals(user.getEmail())) {
                message = "A questão precisa ser de sua autoria!";
            } else if (question.getEstado() != EstadoQuestao.PUBLICADA) {
                message = "Você deve criar uma nova questão para prosseguir!";
            } else {
                message = validateEvaluationQuestion(user, question, module);
            }
        } catch (Exception e) {
            message = "Você deve criar uma nova questão para prosseguir!";
        }

        return message;
    }

    /**
     * Avalia a competência do módulo.
     *
     * @param user    Usuário logado no sistema.
     * @param answers Lista de respostas fornecidas pelo usuário.
     * @param module  Módulo do curso.
     * @return Mensagem de feedback para o usuário.
     */
    private String evaluateCompetence(Usuario user, List<String> answers, ModuloCurso module) {
        String message = null;

        try {
            // TODO: Classificador indisponível
            // Boolean hasCompetence = questaoService.hasCompetencia(module.getNome(),
            // questaoService.getSetCompetencias(respostas.get(0)));
            Boolean hasCompetence = true;
            if (!hasCompetence) {
                message = handleErrorModule(module, "Competencia");
            } else {
                module.setEstado(EstadoModulo.FINALIZADO);
                module.zeraErros();
            }
        } catch (Exception e) {
            message = "Problema interno ao classificar questão, tente novamente!";
        }

        return message;
    }
}
