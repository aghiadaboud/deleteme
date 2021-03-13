package de.hhu.propra.terminplaner.repos;

import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZeitslotRepository extends CrudRepository<Zeitslot, Long> {

  List<Zeitslot> findAll();
}
