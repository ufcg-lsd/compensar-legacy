package springboot.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.model.Competencia;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {

}
