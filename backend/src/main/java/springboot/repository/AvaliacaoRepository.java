package springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import springboot.model.Avaliacao;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends MongoRepository<Avaliacao, String> {
    List<Avaliacao> getAllByQuestao(String questao);
}
