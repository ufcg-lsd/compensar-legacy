package springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.enums.EstadoQuestao;
import springboot.model.Questao;
import springboot.model.Usuario;

@Repository
public abstract interface QuestaoRepository extends MongoRepository<Questao, String> {
	
	Page<Questao> findAll(Pageable pageable);

	Optional<Questao> findById(String id);


	@Query(":#{#query}")
	Page<Questao> getByEnunciadoCompetenciasAutorFonteTipo(@Param("query") String query, Pageable pageable);

	Questao getByEstadoAndQtdAvaliacoesGreaterThan(EstadoQuestao estado, int qtd);

	//@Query("SELECT q FROM Question q WHERE q.qtdAvaliacoes <= 3 AND NOT EXISTS (SELECT a FROM Avaliacao a WHERE a.questao = q AND a.autor = ?1)")
	//@org.springframework.data.jpa.repository.Query(value = "SELECT q FROM questao q", nativeQuery = false)
	//Page<Questao> getPendentes(Usuario usuario, Pageable pageable);

}
