package springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import springboot.model.Conteudo;
import springboot.model.Usuario;

@Repository
public interface ConteudoRepository extends MongoRepository<Conteudo, String> {


}