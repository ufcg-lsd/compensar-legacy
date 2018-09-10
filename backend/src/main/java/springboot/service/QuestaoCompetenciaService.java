
package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import springboot.model.Questao;
import springboot.model.QuestaoCompetencia;
import springboot.model.QuestaoCompetenciaPK;
import springboot.model.QuestaoObjetiva;
import springboot.model.QuestaoSubjetiva;
import springboot.model.Usuario;
import springboot.repository.QuestaoCompetenciaRepository;
import springboot.repository.QuestaoObjetivaRepository;
import springboot.repository.QuestaoSubjetivaRepository;

@Service
public class QuestaoCompetenciaService {

	@Autowired
	private QuestaoObjetivaRepository questaoObjRepository;
	
	@Autowired
	private QuestaoSubjetivaRepository questaoSubjRepository;
	
	@Autowired
	private QuestaoCompetenciaRepository questaoCompetenciaRepository;
	
	public QuestaoCompetencia save(QuestaoCompetencia questaoCompetencia) {
		questaoCompetenciaRepository.save(questaoCompetencia);
		return questaoCompetencia;
	}
	public QuestaoCompetencia delete(Long id_questao, String competencia) {
		QuestaoCompetenciaPK questaoCompetenciaPK = new QuestaoCompetenciaPK(id_questao,competencia);

		Optional<QuestaoCompetencia> optQuestaoCompetencia = questaoCompetenciaRepository.findById(questaoCompetenciaPK);

		/*
		if (!optUsuario.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/

		QuestaoCompetencia questaoCompetencia = optQuestaoCompetencia.get();
		questaoCompetenciaRepository.delete(questaoCompetencia);

		return questaoCompetencia;
	}
	
	public QuestaoCompetencia update(Long id_questao,String competencia, QuestaoCompetencia questaoCompetencia) {
		QuestaoCompetenciaPK questaoCompetenciaPK = new QuestaoCompetenciaPK(id_questao,competencia);

		
		Optional<QuestaoCompetencia> optQuestaoCompetencia = questaoCompetenciaRepository.findById(questaoCompetenciaPK);
		
		/*
		if (!optUsuario.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/

		QuestaoCompetencia novaQuestaoCompetencia = optQuestaoCompetencia.get();
		novaQuestaoCompetencia.setCompetencia(questaoCompetencia.getCompetencia());
		
		questaoCompetenciaRepository.save(novaQuestaoCompetencia);

		return novaQuestaoCompetencia;
	}
	
	public List<QuestaoCompetencia> getAll() {
		return questaoCompetenciaRepository.findAll();
	}
	
	
}
	
	
	
	
	
	/**
	@Autowired
	private CompetenciaRepository competenciaRepository;
	


	public Questao addCompetencia(Long id_competencia, Long id_questao) {
		Questao novaQuestao;
		
		if (isObjetiva(id_questao)) {
			Optional<QuestaoObjetiva> optQuestaoObj = questaoObjRepository.findById(id_questao);
			
			Optional<Competencia> optCompetencia = competenciaRepository.findById(id_competencia);

			QuestaoObjetiva novaQuestaoObj = optQuestaoObj.get();
			
			Competencia competencia = optCompetencia.get();
			
			novaQuestaoObj.addCompetencias(competencia);
			
			questaoObjRepository.save(novaQuestaoObj);
			
			novaQuestao = novaQuestaoObj;
		} else {
			Optional<QuestaoSubjetiva> optQuestaoSubj = questaoSubjRepository.findById(id_questao);
			
			Optional<Competencia> optCompetencia = competenciaRepository.findById(id_competencia);

			QuestaoSubjetiva novaQuestaoSubj = optQuestaoSubj.get();
			
			Competencia competencia = optCompetencia.get();
			
			novaQuestaoSubj.addCompetencias(competencia);
			
			questaoSubjRepository.save(novaQuestaoSubj);
			
			novaQuestao = novaQuestaoSubj;
		}

		/*
		if (!optAlternativa.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/

/**
		return novaQuestao;
	}
	
	private boolean isObjetiva(Long id) {
		return questaoObjRepository.existsById(id);
	}

	public Questao removeCompetencia(Long id_competencia, Long id_questao) {
		Questao novaQuestao;
		
		if (isObjetiva(id_questao)) {
			Optional<QuestaoObjetiva> optQuestaoObj = questaoObjRepository.findById(id_questao);
			
			Optional<Competencia> optCompetencia = competenciaRepository.findById(id_competencia);

			QuestaoObjetiva novaQuestaoObj = optQuestaoObj.get();
			
			Competencia competencia = optCompetencia.get();
			
			novaQuestaoObj.removeCompetencia(competencia);
			
			novaQuestao = novaQuestaoObj;
			
			questaoObjRepository.save(novaQuestaoObj);
			
		} else {
			Optional<QuestaoSubjetiva> optQuestaoSubj = questaoSubjRepository.findById(id_questao);
			
			Optional<Competencia> optCompetencia = competenciaRepository.findById(id_competencia);

			QuestaoSubjetiva novaQuestaoSubj = optQuestaoSubj.get();
			
			Competencia competencia = optCompetencia.get();
			
			novaQuestaoSubj.removeCompetencia(competencia);
			
			novaQuestao = novaQuestaoSubj;
			
			questaoSubjRepository.save(novaQuestaoSubj);
			
		}

		/*
		if (!optAlternativa.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/
	/**
		return novaQuestao;
	}
	
	
}

*/



