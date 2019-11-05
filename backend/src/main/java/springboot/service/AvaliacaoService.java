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
import java.util.Optional;

@Service
public class AvaliacaoService {

    private final String errorMessage = "Avaliação não está cadastrada.";

    @Autowired
    public AvaliacaoRepository avaliacaoRepository;

    public Avaliacao save(Avaliacao avaliacao) {
        avaliacaoRepository.save(avaliacao);
        return avaliacao;
    }

    public Avaliacao update(Avaliacao avaliacao, String id) {
        Optional<Avaliacao> optAvaliacao = avaliacaoRepository.findById(id);

        if (!optAvaliacao.isPresent()) {
            throw new RegisterNotFoundException(errorMessage);
        }

        avaliacao.setId(id);

        if (!avaliacao.getAutor().equals(optAvaliacao.get().getAutor())) {
            throw new PermissionDeniedException("A avaliação foi criada por outro usuário.");
        }

        avaliacaoRepository.save(avaliacao);

        return avaliacao;
    }

    public Avaliacao delete(String id) {
        Optional<Avaliacao> optAvaliacao = avaliacaoRepository.findById(id);

        if (!optAvaliacao.isPresent()) {
            throw new RegisterNotFoundException(errorMessage);
        }

        Avaliacao avaliacao = optAvaliacao.get();
        avaliacaoRepository.delete(avaliacao);

        return avaliacao;
    }

    public Page<Avaliacao> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avaliacaoRepository.findAll(pageable);
    }

    public Avaliacao getById(String id) {
        Optional<Avaliacao> optAvaliacao = avaliacaoRepository.findById(id);

        if (!optAvaliacao.isPresent()) {
            throw new RegisterNotFoundException(errorMessage);
        }

        return optAvaliacao.get();
    }

    public List<Avaliacao> getAllByQuestao(String questao) {
        return avaliacaoRepository.getAllByQuestao(questao);
    }


}
