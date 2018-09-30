
package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.exception.RegisterNotFoundException;
import springboot.model.QuestaoCompetencia;
import springboot.model.QuestaoCompetenciaPK;
import springboot.repository.QuestaoCompetenciaRepository;

@Service
public class QuestaoCompetenciaService {

	private final String errorMessage = "A QuestãoCompetência não está cadastrada!";

	@Autowired
	private QuestaoCompetenciaRepository questaoCompetenciaRepository;

	public QuestaoCompetencia save(QuestaoCompetencia questaoCompetencia) {
		questaoCompetenciaRepository.save(questaoCompetencia);
		return questaoCompetencia;
	}

	public QuestaoCompetencia delete(Long id_questao, String competencia) {
		QuestaoCompetenciaPK questaoCompetenciaPK = new QuestaoCompetenciaPK(id_questao, competencia);

		Optional<QuestaoCompetencia> optQuestaoCompetencia = questaoCompetenciaRepository
				.findById(questaoCompetenciaPK);

		if (!optQuestaoCompetencia.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		QuestaoCompetencia questaoCompetencia = optQuestaoCompetencia.get();
		questaoCompetenciaRepository.delete(questaoCompetencia);

		return questaoCompetencia;
	}

	public QuestaoCompetencia update(Long id_questao, String competencia, QuestaoCompetencia questaoCompetencia) {
		QuestaoCompetenciaPK questaoCompetenciaPK = new QuestaoCompetenciaPK(id_questao, competencia);

		Optional<QuestaoCompetencia> optQuestaoCompetencia = questaoCompetenciaRepository
				.findById(questaoCompetenciaPK);

		if (!optQuestaoCompetencia.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}

		QuestaoCompetencia novaQuestaoCompetencia = optQuestaoCompetencia.get();
		novaQuestaoCompetencia.setCompetencia(questaoCompetencia.getCompetencia());

		questaoCompetenciaRepository.save(novaQuestaoCompetencia);

		return novaQuestaoCompetencia;
	}

	public List<QuestaoCompetencia> getAll() {
		return questaoCompetenciaRepository.findAll();
	}

}