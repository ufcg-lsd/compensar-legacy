package springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import springboot.model.ListaQuestoes;

@Repository
public interface ListaQuestoesRepository extends MongoRepository<ListaQuestoes, String> {

	Page<ListaQuestoes> getByAutor(String autor, Pageable pageable);

}
