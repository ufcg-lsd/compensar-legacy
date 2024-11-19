package springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import springboot.model.Competencia;

@Repository
public interface CompetenciaRepository extends MongoRepository<Competencia, String> {

}