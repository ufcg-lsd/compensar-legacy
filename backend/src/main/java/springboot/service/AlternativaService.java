package springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.model.Alternativa;
import springboot.repository.AlternativaRepository;

@Service
public class AlternativaService {

	@Autowired
	private AlternativaRepository alternativaRepository;
	
	
	public Alternativa save(Alternativa alternativa) {
		alternativaRepository.save(alternativa);
		return alternativa;
	}
	
	public Alternativa delete(Long id) {
		Optional<Alternativa> optAlternativa = alternativaRepository.findById(id);

		/*
		if (!optAluno.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/

		Alternativa alternativa = optAlternativa.get();
		alternativaRepository.delete(alternativa);

		return alternativa;
	}
	
	public Alternativa update(Alternativa alternativa, Long id) {
		Optional<Alternativa> optAlternativa = alternativaRepository.findById(id);

		/*
		if (!optAlternativa.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/
		Alternativa novaAlternativa = optAlternativa.get();
		novaAlternativa.setCorreta(alternativa.isCorreta());
		novaAlternativa.setTexto(alternativa.getTexto());
	

		alternativaRepository.save(novaAlternativa);

		return novaAlternativa;
	}

	public List<Alternativa> getAll() {
		return alternativaRepository.findAll();
	}
	
	public Alternativa getById(Long id) {
		Optional<Alternativa> optAlternativa = alternativaRepository.findById(id);

		/*
		if (!optAlternativa.isPresent()) {
			throw new RegisterNotFoundException(errorMessage);
		}
		*/	
		return optAlternativa.get();
	}




}
