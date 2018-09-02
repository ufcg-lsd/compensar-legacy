package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.model.QuestaoSubjetiva;
import springboot.repository.QuestaoSubjetivaRepository;

@Service
public class QuestaoSubjetivaService {
	
	@Autowired
	private QuestaoSubjetivaRepository questaoSubjRepository;

	public QuestaoSubjetiva save(QuestaoSubjetiva questaoSubj) {
		questaoSubjRepository.save(questaoSubj);
		return questaoSubj;
	}

	public QuestaoSubjetiva delete(Long id) {
	
		Optional<QuestaoSubjetiva> optQuestaoSubj = questaoSubjRepository.findById(id);

		/*
		if (!optAluno.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/

		QuestaoSubjetiva questaoSubj = optQuestaoSubj.get();
		questaoSubjRepository.delete(questaoSubj);

		return questaoSubj;
	}

	public QuestaoSubjetiva update(QuestaoSubjetiva questaoSubj, Long id) {
		
		Optional<QuestaoSubjetiva> optQuestaoSubj = questaoSubjRepository.findById(id);

		/*
		if (!optAlternativa.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/
		QuestaoSubjetiva novaQuestaoSubj = optQuestaoSubj.get();
		novaQuestaoSubj.setFonte(questaoSubj.getFonte());
		novaQuestaoSubj.setAutor(questaoSubj.getAutor());
		novaQuestaoSubj.setImage(questaoSubj.getImagem());
		novaQuestaoSubj.setTipo(questaoSubj.getTipo());
		novaQuestaoSubj.setEnunciado(questaoSubj.getEnunciado());
		novaQuestaoSubj.setEspelho(questaoSubj.getEspelho());

	

		questaoSubjRepository.save(novaQuestaoSubj);

		return novaQuestaoSubj;
	}

	public List<QuestaoSubjetiva> getAll() {

		return questaoSubjRepository.findAll();
	}

	public QuestaoSubjetiva getById(Long id) {
		Optional<QuestaoSubjetiva> optQuestaoSubj = questaoSubjRepository.findById(id);

		/*
		if (!optAlternativa.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/	
		return optQuestaoSubj.get();
	}

}
