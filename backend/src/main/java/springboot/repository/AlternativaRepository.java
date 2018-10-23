package springboot.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import springboot.model.Alternativa;

@Repository
@Transactional
public interface AlternativaRepository extends MongoRepository<Alternativa, Long> {

}
