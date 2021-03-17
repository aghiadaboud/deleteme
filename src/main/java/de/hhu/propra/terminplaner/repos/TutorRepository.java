package de.hhu.propra.terminplaner.repos;

import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TutorRepository extends CrudRepository<Tutor, Long> {

}
