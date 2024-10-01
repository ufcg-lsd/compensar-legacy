package springboot.service;

import org.springframework.stereotype.Service;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.Conteudo;
import springboot.repository.ConteudoRepository;

import java.util.List;

@Service
public class ConteudoService {

    private static final String ERROR_MESSAGE = "Conteúdo ainda não registrado no sistema!";

    private final ConteudoRepository conteudoRepository;

    public ConteudoService(ConteudoRepository conteudoRepository) {
        this.conteudoRepository = conteudoRepository;
    }

    public Conteudo save(Conteudo conteudo) {
        return conteudoRepository.save(conteudo);
    }

    public Conteudo delete(String conteudoText) {
        Conteudo conteudo = findConteudoById(conteudoText);
        conteudoRepository.delete(conteudo);
        return conteudo;
    }

    public List<Conteudo> getAll() {
        return conteudoRepository.findAll();
    }

    public Conteudo getById(String id) {
        return findConteudoById(id);
    }

    private Conteudo findConteudoById(String id) {
        return conteudoRepository.findById(id)
                .orElseThrow(() -> new RegisterNotFoundException(ERROR_MESSAGE));
    }
}
