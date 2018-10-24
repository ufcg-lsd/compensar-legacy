package springboot.repository;

import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.ListaQuestoes;


@Repository
public interface ListaQuestoesRepository extends MongoRepository<ListaQuestoes, Long>{

	  @Query("{ 'email' : ?0 }")
	public Optional<ListaQuestoes> pesquisarPorEmail(@Param("email") String email);

}
