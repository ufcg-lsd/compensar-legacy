package springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springboot.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

}
