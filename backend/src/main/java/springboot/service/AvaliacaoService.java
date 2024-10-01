package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.exception.data.PermissionDeniedException;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.Avaliacao;
import springboot.repository.AvaliacaoRepository;

import java.util.List;

@Service
public class AvaliacaoService {
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    private static final String ERROR_MESSAGE = "Avaliação não está cadastrada.";

    public AvaliacaoService() {
    }

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao save(Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao update(Avaliacao avaliacao, String id) {
        Avaliacao existingAvaliacao = findAvaliacaoById(id);

        if (!avaliacao.getAutor().equals(existingAvaliacao.getAutor())) {
            throw new PermissionDeniedException("A avaliação foi criada por outro usuário.");
        }

        avaliacao.setId(id);
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao delete(String id) {
        Avaliacao avaliacao = findAvaliacaoById(id);
        avaliacaoRepository.delete(avaliacao);
        return avaliacao;
    }

    public Page<Avaliacao> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avaliacaoRepository.findAll(pageable);
    }

    public Avaliacao getById(String id) {
        return findAvaliacaoById(id);
    }

    public List<Avaliacao> getAllByQuestao(String questao) {
        return avaliacaoRepository.getAllByQuestao(questao);
    }

    private Avaliacao findAvaliacaoById(String id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RegisterNotFoundException(ERROR_MESSAGE));
    }
}
