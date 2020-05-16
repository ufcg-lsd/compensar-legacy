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
import springboot.model.*;
import springboot.model.ModuloCurso.EstadoModulo;
import springboot.repository.CompetenciaRepository;
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
    private QuestaoService questaoService;

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
                int index = modulo.getNumeroExemplo();
                moduloOutput.getExemplos().add(c.getExemplos().get(index));
                moduloOutput.getTextoExemplos().add(c.getTextoExemplos().get(index));
                modulo.setNumeroExemplo((index+1) % c.getExemplos().size());
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

        ModuloCurso modAvaliacao = usuario.getCursoAvaliacao().get(9);
        ModuloCursoOutput outputAvaliacao = new ModuloCursoOutput();
        outputAvaliacao.setNome(modAvaliacao.getNome());
        outputAvaliacao.setEstado(modAvaliacao.getEstado());

        if (modAvaliacao.getEstado().equals(EstadoModulo.PRATICA)) {
            modAvaliacao.setQuestoes(new ArrayList<>());
            for(String comp : COMP_NAMES) {
                // Get 2 samples of questions (one with an one without competencia) unordered
                List<Questao> samples = questaoService.getSamples(comp);
                for(Questao q : samples) {
                    modAvaliacao.getQuestoes().add(q.getId());
                    outputAvaliacao.getQuestoes().add(q.getEnunciado());
                }
            }
        }

        if (ativo) {
            outputAvaliacao.setEstado(EstadoModulo.INATIVO);
        }

        usuario.getCursoAvaliacao().set(9, modAvaliacao);
        ret.add(outputAvaliacao);

        usuarioService.update(usuario, usuario.getEmail());

        return new ResponseEntity<CursoOutput>(new CursoOutput(ret, mensagem), HttpStatus.OK);
    }

    @ApiOperation("Cria novo curso de avaliação para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/new", method = RequestMethod.GET)
    public ResponseEntity<CursoOutput> novoCursoAvaliacoes(@RequestAttribute(name="usuario") Usuario usuario) {

        if (usuario.getCursoAvaliacao() == null || usuario.getCursoAvaliacao().size() == 0) {
            usuario.setCursoAvaliacao(new ArrayList<>());

            for(String comp : COMP_NAMES) {
                usuario.getCursoAvaliacao().add(new ModuloCurso(comp));
            }
            usuario.getCursoAvaliacao().add(new ModuloCurso("COMP_AVALIAÇÃO FINAL"));
            usuario.getCursoAvaliacao().get(9).setEstado(EstadoModulo.PRATICA);

            usuarioService.update(usuario, usuario.getEmail());
        }

        return auxCursoAvaliacoes(usuario, null);
    }

    @ApiOperation("Cria novo curso de avaliação para o usuário logado.\r\n")
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
                if (i != 9)
                    c = competenciaRepository.findById(modulo.getNome()).get().getCursoAvaliacao();
                String competencia = modulo.getNome();
                if (modulo.getEstado().equals(EstadoModulo.DESCRICAO)) {
                    modulo.setEstado(EstadoModulo.EXEMPLOS);
                } else if (modulo.getEstado().equals(EstadoModulo.EXEMPLOS)) {
                    modulo.setEstado(EstadoModulo.PRATICA);
                }else if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                    int total = 0;
                    int erros = 0;
                    if (i == usuario.getCursoAvaliacao().size()-1) {
                        total = 9*2;
                        if (respostas == null || respostas.size() != 9*2) {
                            erros += 18;
                        } else {
                            int j = 0;
                            for (String comp : COMP_NAMES) {
                                if (!respostas.get(j).equals(questaoService.evaluateQuestao(comp,modulo.getQuestoes().get(j))))
                                    erros++;
                                j++;
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
                        mensagem = "Você obteve menos de 70% de acerto, tente novamente mais tarde!";
                    } else {
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
                            mensagem = "Você não conseguiu identificar a presença/ausência da competência, tente novamente!";
                        }
                    }

                }
                usuario.getCursoAvaliacao().set(i, modulo);
                if (sendToBack) {
                    usuario.getCursoAvaliacao().add(usuario.getCursoAvaliacao().get(9));
                    usuario.getCursoAvaliacao().remove(i);

                    usuario.getCursoAvaliacao().set(8, modulo);
                }
                usuarioService.update(usuario, usuario.getEmail());
                break;
            }
        }

        return auxCursoAvaliacoes(usuario, mensagem);
    }
}
