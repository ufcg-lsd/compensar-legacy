package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.exception.data.RegisterNotFoundException;
import springboot.model.Conteudo;
import springboot.repository.ConteudoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConteudoService {

    private final String errorMessage = "Conteúdo ainda não registrado no sistema!";

    @Autowired
    private ConteudoRepository conteudoRepository;

    public ConteudoService() {
    }

    public Conteudo save(Conteudo conteudo) {
        conteudoRepository.save(conteudo);
        return conteudo;
    }

    public Conteudo delete(String conteudoText) {
        Optional<Conteudo> optConteudo = conteudoRepository.findById(conteudoText);

        if (!optConteudo.isPresent()) {
            throw new RegisterNotFoundException(errorMessage);
        }

        Conteudo conteudo = optConteudo.get();
        conteudoRepository.delete(conteudo);

        return conteudo;
    }

    public List<Conteudo> getAll() {
        return conteudoRepository.findAll();
    }

    public Conteudo getById(String id) {
        Optional<Conteudo> optConteudo = conteudoRepository.findById(id);

        if (!optConteudo.isPresent()) {
            throw new RegisterNotFoundException(errorMessage);
        }

        Conteudo conteudo = optConteudo.get();
        return conteudo;
    }

}
