package springboot.dto.IO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import springboot.dto.input.ListaQuestoesInput;
import springboot.dto.output.ListaQuestoesOutput;
import springboot.model.ListaQuestoes;
import springboot.model.Questao;
import springboot.model.Usuario;
import springboot.service.QuestaoService;
/**
 * Classe responsável pela conversão entre DTOs e entidades do modelo
 * ListaQuestoes.
 */
@Component
public class ListaQuestoesIO {
    private final QuestaoService questaoService;

    /**
     * Construtor para injeção de dependências.
     *
     * @param questaoService Serviço de questões para operações de busca.
     */
    @Autowired
    public ListaQuestoesIO(QuestaoService questaoService) {
        this.questaoService = questaoService;
    }

    /**
     * Converte um DTO de entrada para uma entidade de modelo.
     *
     * @param listaQuestoesInput Objeto DTO de entrada.
     * @param usuario            Objeto do usuário associado.
     * @return ListaQuestoes Entidade de modelo.
     */
    public ListaQuestoes toEntity(ListaQuestoesInput listaQuestoesInput, Usuario usuario) {
        List<String> questoes = listaQuestoesInput.getQuestoes().stream()
                .map(questaoId -> questaoService.getById(questaoId).getId())
                .collect(Collectors.toList());
        return new ListaQuestoes(listaQuestoesInput.getNomeLista(), usuario.getEmail(), questoes);
    }

    /**
     * Converte uma entidade de modelo para um DTO de saída.
     *
     * @param listaQuestoes Entidade de modelo.
     * @param autor         Autor da lista de questões.
     * @return ListaQuestoesOutput DTO de saída.
     */
    public ListaQuestoesOutput toDto(ListaQuestoes listaQuestoes, String autor) {
        List<Questao> questoes = listaQuestoes.getQuestoes().stream()
                .map(questaoId -> Optional.ofNullable(questaoService.getById(questaoId))
                        .orElseThrow(() -> new IllegalArgumentException("Questão não encontrada: ".concat(questaoId))))
                .collect(Collectors.toList());
        return new ListaQuestoesOutput(listaQuestoes.getId(), listaQuestoes.getNomeLista(), autor, questoes);
    }
}
