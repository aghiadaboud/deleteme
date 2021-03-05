package de.hhu.terminplaner.repos;


import de.hhu.terminplaner.model.Tutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorRepository extends CrudRepository<Tutor, Long> {

    List<Tutor> findAll();
}
