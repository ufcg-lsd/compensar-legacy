package springboot.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.ListaQuestoes;


@Repository
public interface ListaQuestoesRepository extends MongoRepository<ListaQuestoes, String>{

	  
	@Query(":#{#query}")
	Page<ListaQuestoes> getByNomeEmail(@Param("query") String query, Pageable pageable);

}
