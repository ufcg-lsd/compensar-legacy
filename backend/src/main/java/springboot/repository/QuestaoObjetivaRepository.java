package springboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.model.QuestaoObjetiva;

@Repository
public interface QuestaoObjetivaRepository extends JpaRepository<QuestaoObjetiva, Long> {
	
	
	

}
