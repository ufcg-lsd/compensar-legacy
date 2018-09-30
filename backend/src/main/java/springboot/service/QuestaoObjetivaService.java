package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.exception.RegisterNotFoundException;
import springboot.model.QuestaoObjetiva;
import springboot.repository.QuestaoObjetivaRepository;

@Service
public class QuestaoObjetivaService {

	private final String errorMessage = "A questão objetiva não está cadastrada.";

	@Autowired
	private QuestaoObjetivaRepository questaoObjRepository;

	public QuestaoObjetiva save(QuestaoObjetiva questaoObj) {
		questaoObjRepository.save(questaoObj);
		return questaoObj;
	}

	public QuestaoObjetiva delete(Long id) {
		Optional<QuestaoObjetiva> optQuestaoObj = questaoObjRepository.findById(id);

		if (!optQuestaoObj.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		QuestaoObjetiva questaoObj = optQuestaoObj.get();
		questaoObjRepository.delete(questaoObj);

		return questaoObj;
	}

	public QuestaoObjetiva update(QuestaoObjetiva questaoObj, Long id) {
		Optional<QuestaoObjetiva> optQuestaoObj = questaoObjRepository.findById(id);

		if (!optQuestaoObj.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		QuestaoObjetiva novaQuestaoObj = optQuestaoObj.get();
		novaQuestaoObj.setFonte(questaoObj.getFonte());
		novaQuestaoObj.setAutor(questaoObj.getAutor());
		novaQuestaoObj.setImage(questaoObj.getImagem());
		novaQuestaoObj.setTipo(questaoObj.getTipo());
		novaQuestaoObj.setEnunciado(questaoObj.getEnunciado());
		novaQuestaoObj.setAlternativas(questaoObj.getAlternativas());

		questaoObjRepository.save(novaQuestaoObj);

		return novaQuestaoObj;
	}

	public List<QuestaoObjetiva> getAll() {
		return questaoObjRepository.findAll();
	}

	public QuestaoObjetiva getById(Long id) {
		Optional<QuestaoObjetiva> optQuestaoObj = questaoObjRepository.findById(id);

		if (!optQuestaoObj.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		return optQuestaoObj.get();
	}

}
