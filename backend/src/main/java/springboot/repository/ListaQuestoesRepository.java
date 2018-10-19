package springboot.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.ListaQuestoes;


@Repository
@Transactional
public interface ListaQuestoesRepository extends JpaRepository<ListaQuestoes, Long>{

	@Query("SELECT a FROM ListaQuestoes a WHERE LOWER(a.email) LIKE CONCAT('%', LOWER(:email), '%')")
	public Optional<ListaQuestoes> pesquisarPorEmail(@Param("email") String email);

}
