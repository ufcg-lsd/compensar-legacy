package springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import springboot.model.Alternativa;
import springboot.repository.AlternativaRepository;

public class AlternativaService {

	@Autowired
	private AlternativaRepository alternativaRepository;
	
	
	public Alternativa save(Alternativa alternativa) {
		alternativaRepository.save(alternativa);
		return alternativa;
	}

	public Alternativa getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Alternativa delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Alternativa> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Alternativa update(Alternativa alternativa, Long id) {
		// TODO Auto-generated method stub
		return null;
	}


}
