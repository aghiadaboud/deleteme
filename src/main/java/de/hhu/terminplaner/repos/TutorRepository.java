package de.hhu.terminplaner.repos;


import de.hhu.terminplaner.model.Tutor;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends CrudRepository<Tutor, Long> {

  List<Tutor> findAll();
}
