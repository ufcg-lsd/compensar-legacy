package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.model.Competencia;
import springboot.repository.CompetenciaRepository;

@Service
public class CompetenciaService {

	@Autowired
	private CompetenciaRepository competenciaRepository;

	public Competencia save(Competencia competencia) {
		competenciaRepository.save(competencia);
		return competencia;
	}

	public Competencia delete(Long id) {
		Optional<Competencia> optCompetencia = competenciaRepository.findById(id);

		/*
		if (!optAluno.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/

		Competencia competencia = optCompetencia.get();
		competenciaRepository.delete(competencia);

		return competencia;
	}

	public Competencia update(Competencia competencia, Long id) {
		Optional<Competencia> optCompetencia = competenciaRepository.findById(id);

		/*
		if (!optAlternativa.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/
		Competencia novaCompetencia = optCompetencia.get();
		novaCompetencia.setTipo(competencia.getTipo());

		competenciaRepository.save(novaCompetencia);

		return novaCompetencia;
	}

	public List<Competencia> getAll() {
		return competenciaRepository.findAll();
	}

	public Competencia getById(Long id) {
		Optional<Competencia> optCompetencia = competenciaRepository.findById(id);

		/*
		if (!optAlternativa.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/	
		return optCompetencia.get();
	}
}
