package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.Zeitslot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZeitslotRepository extends CrudRepository<Zeitslot, Long> {

    List<Zeitslot> findAll();

}
