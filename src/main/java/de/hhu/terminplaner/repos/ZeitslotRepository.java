package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.zeitslot.Zeitslot;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZeitslotRepository extends CrudRepository<Zeitslot, Long> {

  List<Zeitslot> findAll();
}
