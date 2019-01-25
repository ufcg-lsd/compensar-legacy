package springboot.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.exception.RegisterNotFoundException;
import springboot.model.Questao;
import springboot.repository.QuestaoRepository;

@Service
public class QuestaoService {

	private final String errorMessage = "A questão subjetiva não está cadastrada.";

	@Autowired
	private QuestaoRepository questaoRepository;

	public Questao save(Questao questao) {

		// Aqui chama o classificador e atualiza o objeto questao

		questaoRepository.save(questao);
		return questao;
	}

	public Questao delete(String id) {
		Optional<Questao> optQuestao = questaoRepository.findById(id);

		if (!optQuestao.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		Questao questao = optQuestao.get();

		questaoRepository.delete(questao);
		return questao;
	}

	public Questao update(Questao questao, String id) {

		Optional<Questao> optQuestao = questaoRepository.findById(id);

		if (!optQuestao.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		Questao novaQuestao = optQuestao.get();
		novaQuestao.setFonte(questao.getFonte());
		novaQuestao.setAutor(questao.getAutor());
		novaQuestao.setTipo(questao.getTipo());
		novaQuestao.setEnunciado(questao.getEnunciado());
		novaQuestao.setCompetencias(questao.getCompetencias());
		novaQuestao.setEspelho(questao.getEspelho());
		novaQuestao.setAlternativas(questao.getAlternativas());

		questaoRepository.save(novaQuestao);

		return novaQuestao;
	}

	public List<Questao> getAll() {

		return questaoRepository.findAll();
	}

	public Questao getById(String id) {
		Optional<Questao> optQuestao = questaoRepository.findById(id);

		if (!optQuestao.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		return optQuestao.get();
	}

	public List<Questao> getByEnunciadoCompetencias(String enunciado, HashSet<String> competencias) {
		return questaoRepository.getByEnunciadoCompetencias(enunciado, competencias);
	}

	public List<Questao> getByEnunciado(String enunciado) {
		return questaoRepository.getByEnunciado(enunciado);
	}

}
