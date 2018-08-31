package springboot.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.model.QuestaoObjetiva;

@Repository
@Transactional
public interface QuestaoObjetivaRepository extends JpaRepository<QuestaoObjetiva, Long> {
	
	
	

}
