package springboot.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.model.ListaQuestoes;
import springboot.model.Usuario;


@Repository
public interface ListaQuestoesRepository extends MongoRepository<ListaQuestoes, String>{

	Page<ListaQuestoes> getByAutor(String autor, Pageable pageable);

}
