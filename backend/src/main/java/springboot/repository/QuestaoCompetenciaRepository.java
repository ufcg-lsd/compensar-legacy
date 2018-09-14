package springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.Questao;
import springboot.model.QuestaoCompetencia;
import springboot.model.QuestaoCompetenciaPK;

@Repository
public interface QuestaoCompetenciaRepository extends JpaRepository<QuestaoCompetencia, QuestaoCompetenciaPK> {

	/*
	@Query("SELECT q FROM Questao q, QuestaoCompetencia qp WHERE q.`id`= qp.`id_questao` AND LOWER(qp.`competencia`) LIKE CONCAT('%', LOWER(:competencia), '%')")
	public List<Questao> pesquisarQuestaoPorCompetencia(@Param("competencia") String competencia);
	*/
}