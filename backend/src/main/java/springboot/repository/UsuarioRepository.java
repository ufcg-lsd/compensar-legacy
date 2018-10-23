package springboot.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springboot.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	/*
	 * @Query("SELECT d FROM Disciplina d WHERE LOWER(d.nome) LIKE CONCAT('%', LOWER(:nome), '%')"
	 * ) public List<Usuario> pesquisarPorNome(@Param("nome") String nome);
	 */

	Usuario findByEmail(String email);

}
