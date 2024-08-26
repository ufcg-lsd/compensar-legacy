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
import springboot.dto.output.QuestaoOutput;
import springboot.enums.EstadoQuestao;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.*;
import springboot.model.ModuloCurso.EstadoModulo;
import springboot.repository.CompetenciaRepository;
import springboot.service.AvaliacaoService;
import springboot.service.QuestaoService;
import springboot.service.UsuarioService;

import java.io.IOException;
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

    @ApiOperation("Permite pegar a tela atual do usuário no curso de avaliação.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/", method = RequestMethod.GET)
    public ResponseEntity<CursoOutput> getCursoAvaliacoes(@RequestAttribute(name="usuario") Usuario usuario) {
        return auxCursoAvaliacoes(usuario, null);
    }


    public ResponseEntity<CursoOutput> auxCursoAvaliacoes(@RequestAttribute(name="usuario") Usuario usuario, String mensagem) {
        if (usuario.getCursoAvaliacao() == null || usuario.getCursoAvaliacao().size() == 0) {
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);
        }

        List<ModuloCursoOutput> ret = new ArrayList<>();

        boolean ativo = false;
        for (int i = 0; i < usuario.getCursoAvaliacao().size()-1; i++) {
            ModuloCurso modulo = usuario.getCursoAvaliacao().get(i);
            Curso c = competenciaRepository.findById(modulo.getNome()).get().getCursoAvaliacao();
            ModuloCursoOutput moduloOutput = new ModuloCursoOutput();
            moduloOutput.setNome(modulo.getNome());
            moduloOutput.setDescricao(c.getDescricao());
            moduloOutput.setEstado(modulo.getEstado());
            moduloOutput.setVideo(c.getVideo());
            if (modulo.getEstado().equals(EstadoModulo.EXEMPLOS)) {
                modulo.setNumeroExemplo((modulo.getNumeroExemplo()+1) % c.getExemplos().size());
                int index = modulo.getNumeroExemplo();
                moduloOutput.getExemplos().add(c.getExemplos().get(index));
                moduloOutput.getTextoExemplos().add(c.getTextoExemplos().get(index));
            }
            if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                int index = modulo.getNumeroExemplo();
                moduloOutput.getExemplos().add(c.getExemplos().get(index));
                moduloOutput.getTextoExemplos().add(c.getTextoExemplos().get(index));
                modulo.setQuestoes(new ArrayList<>());
                // Get 2 samples of questions (one with an one without competencia) unordered
                List<Questao> samples = questaoService.getSamples(modulo.getNome());

                for(Questao q : samples) {
                    modulo.getQuestoes().add(q.getId());
                    moduloOutput.getQuestoes().add(q.getEnunciado());
                }
            }
            if (modulo.getEstado().equals(EstadoModulo.FINALIZADO)) {
                moduloOutput.setExemplos(c.getExemplos());
                moduloOutput.setTextoExemplos(c.getTextoExemplos());
            }
            if (ativo) {
                moduloOutput.setEstado(EstadoModulo.INATIVO);
            }
            if (moduloOutput.getEstado() != EstadoModulo.FINALIZADO) {
                ativo = true;
            }
            usuario.getCursoAvaliacao().set(i, modulo);
            ret.add(moduloOutput);
        }

        ModuloCurso modAvaliacao = usuario.getCursoAvaliacao().get(10);
        ModuloCursoOutput outputAvaliacao = new ModuloCursoOutput();
        outputAvaliacao.setNome(modAvaliacao.getNome());
        outputAvaliacao.setEstado(modAvaliacao.getEstado());

        if (modAvaliacao.getEstado().equals(EstadoModulo.PRATICA)) {
            modAvaliacao.setQuestoes(new ArrayList<>());
            for(String comp : COMP_NAMES) {
                // Get 2 samples of questions (one with an one without competencia) unordered
                Questao sample = questaoService.getSample(comp);
                modAvaliacao.getQuestoes().add(sample.getId());
                outputAvaliacao.getQuestoes().add(sample.getEnunciado());
            }
        }

        if (ativo) {
            outputAvaliacao.setEstado(EstadoModulo.INATIVO);
        }

        usuario.getCursoAvaliacao().set(10, modAvaliacao);
        ret.add(outputAvaliacao);

        usuario = usuarioService.update(usuario, usuario.getEmail());

        return new ResponseEntity<CursoOutput>(new CursoOutput(mensagem, ret, null), HttpStatus.OK);
    }

    @ApiOperation("Cria novos cursos para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursos/", method = RequestMethod.GET)
    public ResponseEntity<CursoOutput> novosCursos(@RequestAttribute(name="usuario") Usuario usuario) {

        if (usuario.getCursoAvaliacao() == null || usuario.getCursoAvaliacao().size() == 0) {
            usuario.setCursoAvaliacao(new ArrayList<>());

            usuario.getCursoAvaliacao().add(new ModuloCurso("COMP_INTRODUÇÃO"));

            for(String comp : COMP_NAMES) {
                usuario.getCursoAvaliacao().add(new ModuloCurso(comp));
            }
            usuario.getCursoAvaliacao().add(new ModuloCurso("COMP_AVALIAÇÃO FINAL"));
            usuario.getCursoAvaliacao().get(10).setEstado(EstadoModulo.PRATICA);

            usuario = usuarioService.update(usuario, usuario.getEmail());
        }

        if (usuario.getCursoCriacao() == null || usuario.getCursoCriacao().size() == 0) {
            usuario.setCursoCriacao(new ArrayList<>());

            usuario.getCursoCriacao().add(new ModuloCurso("COMP_INTRODUÇÃO"));

            for(String comp : COMP_NAMES) {
                usuario.getCursoCriacao().add(new ModuloCurso(comp));
            }
            //TODO mudar visualização da avaliação final
            ModuloCurso finalModule = new ModuloCurso("COMP_AVALIAÇÃO FINAL");
            usuario.getCursoCriacao().add(finalModule);
            usuario.getCursoCriacao().get(10).setEstado(EstadoModulo.PRATICA);
            usuario = usuarioService.update(usuario, usuario.getEmail());
        }

        List<ModuloCursoOutput> avaliacaoOutput = auxCursoAvaliacoes(usuario, null).getBody().getCursoAvaliacao();
        List<ModuloCursoOutput> criacaoOutput = auxCursoCriacao(usuario, null).getBody().getCursoCriacao();

        return new ResponseEntity<CursoOutput>(new CursoOutput(null, avaliacaoOutput, criacaoOutput), HttpStatus.OK);
    }

    @ApiOperation("Avança no curso de avaliação para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> avancarCursoAvaliacoes(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody List<Boolean> respostas) {

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
                }else if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                    int total = 0;
                    int erros = 0;
                    if (i == usuario.getCursoAvaliacao().size()-1) {
                        total = 9;
                        if (respostas == null || respostas.size() != 9) {
                            erros += 9;
                        } else {
                            int j = 0;
                            for (String comp : COMP_NAMES) {
                                if (!respostas.get(j).equals(questaoService.evaluateQuestao(comp,modulo.getQuestoes().get(j))))
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
                    //Verifica se errou no máximo 30%
                    if (100*erros <= total*30) {
                        modulo.setEstado(EstadoModulo.FINALIZADO);
                        modulo.zeraErros();
                    } else if (i == usuario.getCursoAvaliacao().size()-1) {
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
                            //processa devido ao numero de erros
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

        return auxCursoAvaliacoes(usuario, mensagem);
    }

    @ApiOperation("Permite pegar a tela atual do usuário no curso de criação de questões.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoCriacao/", method = RequestMethod.GET)
    public ResponseEntity<CursoOutput> getCursoCriacao(@RequestAttribute(name="usuario") Usuario usuario) {
        return auxCursoCriacao(usuario, null);
    }

    public ResponseEntity<CursoOutput> auxCursoCriacao(@RequestAttribute(name="usuario") Usuario usuario, String mensagem) {
        if (usuario.getCursoCriacao() == null || usuario.getCursoCriacao().size() == 0) {
            return new ResponseEntity<CursoOutput>(new CursoOutput(), HttpStatus.NOT_FOUND);
        }

        List<ModuloCursoOutput> ret = new ArrayList<>();

        boolean ativo = false;
        for (int i = 0; i < usuario.getCursoCriacao().size()-1; i++) {
            ModuloCurso modulo = usuario.getCursoCriacao().get(i);
            Curso c = competenciaRepository.findById(modulo.getNome()).get().getCursoCriacao();
            ModuloCursoOutput moduloOutput = new ModuloCursoOutput();
            moduloOutput.setNome(modulo.getNome());
            moduloOutput.setDescricao(c.getDescricao());
            moduloOutput.setEstado(modulo.getEstado());
            moduloOutput.setVideo(c.getVideo());
            if (modulo.getEstado().equals(EstadoModulo.EXEMPLOS)) {
                modulo.setNumeroExemplo((modulo.getNumeroExemplo()+1) % c.getExemplos().size());
                int index = modulo.getNumeroExemplo();
                moduloOutput.getExemplos().add(c.getExemplos().get(index));
                moduloOutput.getTextoExemplos().add(c.getTextoExemplos().get(index));
            }
            if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                int index = modulo.getNumeroExemplo();
                moduloOutput.getExemplos().add(c.getExemplos().get(index));
                moduloOutput.getTextoExemplos().add(c.getTextoExemplos().get(index));
            }
            if (modulo.getEstado().equals(EstadoModulo.FINALIZADO)) {
                moduloOutput.setExemplos(c.getExemplos());
                moduloOutput.setTextoExemplos(c.getTextoExemplos());
            }
            if (ativo) {
                moduloOutput.setEstado(EstadoModulo.INATIVO);
            }
            if (moduloOutput.getEstado() != EstadoModulo.FINALIZADO) {
                ativo = true;
            }
            usuario.getCursoCriacao().set(i, modulo);
            ret.add(moduloOutput);
        }


        ModuloCursoOutput outputAvaliacao = new ModuloCursoOutput();

        ModuloCurso modAvaliacao = usuario.getCursoCriacao().get(10);
        outputAvaliacao.setNome(modAvaliacao.getNome());
        outputAvaliacao.setEstado(modAvaliacao.getEstado());

        if (modAvaliacao.getEstado().equals(EstadoModulo.PRATICA)) {
            if (modAvaliacao.getQuestoes().size() > 0) {
                try {
                    String questao = modAvaliacao.getQuestoes().get(0);
                    outputAvaliacao.getQuestoesDetalhadas().add(questionSearchController.getById(usuario, questao));
                    outputAvaliacao.getQuestoes().add(questao);
                } catch (Exception e) {
                    usuario.getCursoCriacao().get(10).getQuestoes().clear();
                }
            }
        }

        if (ativo) {
            outputAvaliacao.setEstado(EstadoModulo.INATIVO);
        }


        ret.add(outputAvaliacao);

        usuario = usuarioService.update(usuario, usuario.getEmail());

        return new ResponseEntity<CursoOutput>(new CursoOutput(mensagem, null, ret), HttpStatus.OK);
    }

    @ApiOperation("Avança no curso de criação de questões para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoCriacao/", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> avancarCursoCriacao(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody List<String> respostas) {

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
                }else if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                    if (i != 10) {
                        try {
                            Boolean hasCompetencia = questaoService.hasCompetencia(competencia, questaoService.getSetCompetencias(respostas.get(0)));
                            if(!hasCompetencia) {
                                modulo.addErro();
                                if (modulo.getErros() == 2) {
                                    mensagem = "Percebemos que você teve dificuldade com essa competência, então volte e veja algum exemplo";
                                    modulo.setEstado(EstadoModulo.EXEMPLOS);
                                } else if (modulo.getErros() == 3) {
                                    mensagem = "Como você não conseguiu identificar bem a competência, veja uma nova competência e depois voltamos para essa";
                                    sendToBack = true;
                                    modulo.zeraErros();
                                    modulo.setEstado(EstadoModulo.DESCRICAO);
                                    //processa devido ao numero de erros
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
                                for(Avaliacao a : avaliacoes) {
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

        return auxCursoCriacao(usuario, mensagem);
    }

    @ApiOperation("Registra questão criada na avaliação final do usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoCriacao/newQuestion", method = RequestMethod.POST)
    public ResponseEntity<CursoOutput> registraCriacaoQuestao(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody String questao) {

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

        return auxCursoCriacao(usuario, mensagem);
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
