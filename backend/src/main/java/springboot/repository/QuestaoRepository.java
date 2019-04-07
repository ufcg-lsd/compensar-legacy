package springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.Questao;

@Repository
public abstract interface QuestaoRepository extends MongoRepository<Questao, String> {
	
	Page<Questao> findAll(Pageable pageable);

	Optional<Questao> findById(String id);
	
	@Query(":#{#query}")
	Page<Questao> getByEnunciadoCompetenciasAutorFonteTipo(@Param("query") String query, Pageable pageable);
	
	
	
}
