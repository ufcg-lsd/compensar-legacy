package springboot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.enums.EstadoQuestao;
import springboot.model.Questao;

@Repository
public abstract interface QuestaoRepository extends MongoRepository<Questao, String> {
	Page<Questao> findAllByEstado(EstadoQuestao estado, Pageable pageable);

	@Query(":#{#query}")
	Page<Questao> getByEnunciadoCompetenciasAutorFonteTipo(@Param("query") String query, Pageable pageable);

	List<Questao> getByEstadoAndQtdAvaliacoesGreaterThan(EstadoQuestao estado, int qtd);
}
