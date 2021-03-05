package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.model.Uebung;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UebungRepository extends CrudRepository<Uebung, Long> {


}
