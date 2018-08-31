package springboot.repository;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import springboot.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository  extends JpaRepository<Usuario, String>{

	/*
	@Query("SELECT d FROM Disciplina d WHERE LOWER(d.nome) LIKE CONCAT('%', LOWER(:nome), '%')")
	public List<Usuario> pesquisarPorNome(@Param("nome") String nome);
	*/
	
}
