package springboot.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.model.Alternativa;

@Repository
@Transactional
public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {

}
