package springboot.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import springboot.model.Questao;

@Repository
public interface QuestaoRepository extends MongoRepository<Questao, String> {

	Optional<Questao> findById(String id);

	/*
	 * @Query("SELECT q FROM QuestaoSubjetiva q WHERE LOWER(q.autor) LIKE CONCAT('%', LOWER(:autor), '%')"
	 * ) public List<QuestaoSubjetiva> pesquisarPorAutor(@Param("autor") String
	 * autor);
	 * 
	 * @Query("SELECT q FROM QuestaoSubjetiva q WHERE LOWER(q.fonte) LIKE CONCAT('%', LOWER(:fonte), '%')"
	 * ) public List<QuestaoSubjetiva> pesquisarPorFonte(@Param("fonte") String
	 * fonte);
	 */

	@Query("{$text: {$search: ?0}}, {score: {$meta: \"textScore\"}}.sort({score:{$meta:\"textScore\"}})")
	public List<Questao> getByEnunciado(String enunciado);

	@Query("{'$or':[{competencias:  {$all: ?1},$text: {$search: ?0}}]}, {score: {$meta: \"textScore\"}}.sort({score:{$meta:\"textScore\"}})")
	public List<Questao> getByEnunciadoCompetencias(String enunciado, HashSet<String> competencias);

}
