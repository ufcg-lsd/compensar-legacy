package springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import springboot.model.UsuarioPermissao;

@Repository
public interface UsuarioPermissaoRepository extends MongoRepository<UsuarioPermissao, String> {

}
