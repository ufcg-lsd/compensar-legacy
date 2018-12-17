package springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.QuestaoObjetiva;

@Repository
public interface QuestaoObjetivaRepository extends MongoRepository<QuestaoObjetiva, String> {
	
	Optional<QuestaoObjetiva> findById(String id);


	/*
	 * @Query("SELECT q FROM QuestaoObjetiva q WHERE LOWER(q.autor) LIKE CONCAT('%', LOWER(:autor), '%')"
	 * ) public List<QuestaoObjetiva> pesquisarPorAutor(@Param("autor") String
	 * autor);
	 * 
	 * @Query("SELECT q FROM QuestaoObjetiva q WHERE LOWER(q.fonte) LIKE CONCAT('%', LOWER(:fonte), '%')"
	 * ) public List<QuestaoObjetiva> pesquisarPorFonte(@Param("fonte") String
	 * fonte);
	 */

}
