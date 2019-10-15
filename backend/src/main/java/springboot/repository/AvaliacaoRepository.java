package springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import springboot.model.Avaliacao;

@Repository
public interface AvaliacaoRepository extends MongoRepository<Avaliacao, String> {

}
