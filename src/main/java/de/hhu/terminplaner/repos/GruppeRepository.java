package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.Gruppe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GruppeRepository extends CrudRepository<Gruppe, Long> {

    List<Gruppe> findAll();
}
