package springboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.model.QuestaoCompetencia;
import springboot.model.QuestaoCompetenciaPK;

@Repository
public interface QuestaoCompetenciaRepository extends JpaRepository<QuestaoCompetencia, QuestaoCompetenciaPK> {
	
	
	

}