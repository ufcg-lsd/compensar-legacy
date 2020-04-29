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
import springboot.dto.output.CursoAvaliacaoOutput;
import springboot.dto.output.ModuloCursoOutput;
import springboot.model.*;
import springboot.model.ModuloCurso.EstadoModulo;
import springboot.repository.CompetenciaRepository;
import springboot.service.QuestaoService;
import springboot.service.UsuarioService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public ResponseEntity<List<ModuloCursoOutput> > getCursoAvaliacoes(@RequestAttribute(name="usuario") Usuario usuario) {
        if (usuario.getCursoAvaliacao() == null || usuario.getCursoAvaliacao().size() == 0) {
            return new ResponseEntity<List<ModuloCursoOutput> >(new ArrayList<ModuloCursoOutput>(), HttpStatus.NOT_FOUND);
        }

        List<ModuloCursoOutput> ret = new ArrayList<>();


        for (int i = 0; i < usuario.getCursoAvaliacao().size(); i++) {
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
                modulo.setNumeroExemplo((index+1) % c.getExemplos().size());
            }else if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                modulo.setQuestoes(new ArrayList<>());
                // Get 2 samples of questions (one with an one without competencia) unordered
                List<Questao> samples = questaoService.getSamples(modulo.getNome());

                for(Questao q : samples) {
                    modulo.getQuestoes().add(q.getId());
                    moduloOutput.getQuestoes().add(q.getEnunciado());
                }
            } else if (modulo.getEstado().equals(EstadoModulo.FINALIZADO)) {
                moduloOutput.setExemplos(c.getExemplos());
            }
            usuario.getCursoAvaliacao().set(i, modulo);
            ret.add(moduloOutput);
        }

        usuarioService.update(usuario, usuario.getEmail());

        return new ResponseEntity<List<ModuloCursoOutput> >(ret, HttpStatus.OK);
    }

    @ApiOperation("Cria novo curso de avaliação para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/new", method = RequestMethod.GET)
    public ResponseEntity<List<ModuloCursoOutput> > novoCursoAvaliacoes(@RequestAttribute(name="usuario") Usuario usuario) {

        if (usuario.getCursoAvaliacao() == null || usuario.getCursoAvaliacao().size() == 0) {
            usuario.setCursoAvaliacao(new ArrayList<>());

            for(String comp : COMP_NAMES) {
                usuario.getCursoAvaliacao().add(new ModuloCurso(comp));
            }

            usuarioService.update(usuario, usuario.getEmail());
        }

        return getCursoAvaliacoes(usuario);
    }

    @ApiOperation("Cria novo curso de avaliação para o usuário logado.\r\n")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Usuario.class) })
    @RequestMapping(value = "/cursoAvaliacao/", method = RequestMethod.POST)
    public ResponseEntity<List<ModuloCursoOutput> > avancarCursoAvaliacoes(@RequestAttribute(name="usuario") Usuario usuario, @RequestBody List<Boolean> respostas) {

        if (usuario.getCursoAvaliacao() == null || usuario.getCursoAvaliacao().size() == 0) {
            return new ResponseEntity<List<ModuloCursoOutput> >(new ArrayList<ModuloCursoOutput>(), HttpStatus.NOT_FOUND);
        }

        for (int i = 0; i < usuario.getCursoAvaliacao().size(); i++) {
            ModuloCurso modulo = usuario.getCursoAvaliacao().get(i);
            if (!modulo.getEstado().equals(EstadoModulo.FINALIZADO)) {
                boolean sendToBack = false;
                Curso c = competenciaRepository.findById(modulo.getNome()).get().getCursoAvaliacao();
                String competencia = modulo.getNome();
                if (modulo.getEstado().equals(EstadoModulo.DESCRICAO)) {
                    modulo.setEstado(EstadoModulo.EXEMPLOS);
                } else if (modulo.getEstado().equals(EstadoModulo.EXEMPLOS)) {
                    modulo.setEstado(EstadoModulo.PRATICA);
                }else if (modulo.getEstado().equals(EstadoModulo.PRATICA)) {
                    List<Boolean> l1 = questaoService.evaluateQuestoes(competencia, modulo.getQuestoes());
                    if (respostas == null || respostas.size() != 2 || !l1.equals(respostas)) {
                        modulo.addErro();
                        if (modulo.getErros() == 2) {
                            modulo.setEstado(EstadoModulo.EXEMPLOS);
                        } else if (modulo.getErros() == 3) {
                            sendToBack = true;
                            modulo.zeraErros();
                            modulo.setEstado(EstadoModulo.DESCRICAO);
                            //processa devido ao numero de erros
                        }
                    } else {
                        modulo.setEstado(EstadoModulo.FINALIZADO);
                        modulo.zeraErros();
                    }
                }
                usuario.getCursoAvaliacao().set(i, modulo);
                if (sendToBack) {
                    usuario.getCursoAvaliacao().remove(i);
                    usuario.getCursoAvaliacao().add(modulo);
                }
                usuarioService.update(usuario, usuario.getEmail());
                break;
            }
        }

        return getCursoAvaliacoes(usuario);
    }
}
