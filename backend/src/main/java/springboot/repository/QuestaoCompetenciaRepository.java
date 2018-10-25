package springboot.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import springboot.model.QuestaoCompetencia;
import springboot.model.QuestaoCompetenciaPK;

@Repository
public interface QuestaoCompetenciaRepository extends MongoRepository<QuestaoCompetencia, QuestaoCompetenciaPK> {

	/*
	 * @Query("SELECT q FROM Questao q, QuestaoCompetencia qp WHERE q.`id`= qp.`id_questao` AND LOWER(qp.`competencia`) LIKE CONCAT('%', LOWER(:competencia), '%')"
	 * ) public List<Questao> pesquisarQuestaoPorCompetencia(@Param("competencia")
	 * String competencia);
	 */
}