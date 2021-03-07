package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.Termin;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminRepository extends CrudRepository<Termin, Long> {

  List<Termin> findAll();
}
