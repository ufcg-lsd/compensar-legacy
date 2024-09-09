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
    private UsuarioService usuarioService;

    @Autowired
    QuestionSearchController questionSearchController;

    @Autowired
    private QuestaoService questaoService;

    /**
     * Processa a lista de módulos e gera uma lista de objetos de saída
     * correspondentes.
     * 
     * @param modules Lista de módulos a ser processada.
     * @return Lista de objetos `ModuloCursoOutput` gerados a partir dos módulos.
     */
    public List<ModuloCursoOutput> processModules(List<ModuloCurso> modules, CourseType courseType) {
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
        ModuloCursoOutput evaluationOutput = processFinalModule(evaluationMod);

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
     * @param evaluationMod O módulo final de avaliação.
     * @return Um objeto `ModuloCursoOutput` com as informações do módulo final.
     */
    private ModuloCursoOutput processFinalModule(ModuloCurso evaluationMod) {
        ModuloCursoOutput evaluationOutput = new ModuloCursoOutput();
        evaluationOutput.setNome(evaluationMod.getNome());
        evaluationOutput.setEstado(evaluationMod.getEstado());

        if (evaluationMod.getEstado().equals(EstadoModulo.PRATICA)) {
            /********** Dois tratamentos distintos *********** TODO */
            // CURSO CRIAÇÃO
            // if (evaluationMod.getQuestoes().size() > 0) {

            // try {

            // String questao = evaluationMod.getQuestoes().get(0);

            // evaluationOutput.getQuestoesDetalhadas().add(questionSearchController.getById(usuario,
            // questao));

            // evaluationOutput.getQuestoes().add(questao);

            // } catch (Exception e) {

            // usuario.getCursoCriacao().get(10).getQuestoes().clear();

            // }

            /********** CURSO AVALIAÇÃO ****************8 */

            // evaluationMod.setQuestoes(new ArrayList<>());

            // for(String comp : COMP_NAMES) {

            // // Get 2 samples of questions (one with an one without competencia) unordered

            // Questao sample = questaoService.getSample(comp);

            // evaluationMod.getQuestoes().add(sample.getId());

            // evaluationOutput.getQuestoes().add(sample.getEnunciado());
        }

        return evaluationOutput;
    }

    /**
     * Cria um objeto `ModuloCursoOutput` a partir de um módulo de curso e seu
     * respectivo `Curso`.
     * 
     * @param module O módulo de curso a ser convertido.
     * @param course O curso associado ao módulo.
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
     * @param course
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
     * @param modulo O módulo de curso a ser atualizado.
     * @param curso  O curso associado ao módulo.
     * @param output O objeto de saída a ser atualizado.
     * @param courseType O tipo de curso acessado.
     */
    @SuppressWarnings("unlikely-arg-type")
    private void addPracticeDetails(ModuloCurso modulo, Curso curso, ModuloCursoOutput output, CourseType courseType) {
        int index = modulo.getNumeroExemplo();
        output.getExemplos().add(curso.getExemplos().get(index));
        output.getTextoExemplos().add(curso.getTextoExemplos().get(index));

        if (courseType.equals("AVALIACAO")) {
            List<Questao> samples = questaoService.getSamples(modulo.getNome());

            for (Questao q : samples) {
                modulo.getQuestoes().add(q.getId());
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

        List<ModuloCursoOutput> outputList = processModules(user.getCursoAvaliacao(), CourseType.AVALIACAO);

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

    public ResponseEntity<CursoOutput> auxCursoCriacao(@RequestAttribute(name = "usuario") Usuario user,
            String message) {
        if (isCourseListEmpty(user.getCursoCriacao()))
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);

        List<ModuloCursoOutput> outputList = processModules(user.getCursoCriacao(), CourseType.CRIACAO);

        user = usuarioService.update(user, user.getEmail());

        return new ResponseEntity<CursoOutput>(new CursoOutput(message, null, outputList), HttpStatus.OK);
    }
}
