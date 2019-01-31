package springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.Questao;

@Repository
public abstract interface QuestaoRepository extends MongoRepository<Questao, String> {

	Optional<Questao> findById(String id);
	
	@Query(":#{#query}")
	public List<Questao> getByEnunciadoCompetenciasAutorFonteTipo(@Param("query") String query);
	
	
	
}
