package springboot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.QuestaoSubjetiva;

@Repository
public interface QuestaoSubjetivaRepository extends JpaRepository<QuestaoSubjetiva, Long>{
	
	/*
	@Query("SELECT q FROM QuestaoSubjetiva q WHERE LOWER(q.autor) LIKE CONCAT('%', LOWER(:autor), '%')")
	public List<QuestaoSubjetiva> pesquisarPorAutor(@Param("autor") String autor);
	
	@Query("SELECT q FROM QuestaoSubjetiva q WHERE LOWER(q.fonte) LIKE CONCAT('%', LOWER(:fonte), '%')")
	public List<QuestaoSubjetiva> pesquisarPorFonte(@Param("fonte") String fonte);
	*/

}
