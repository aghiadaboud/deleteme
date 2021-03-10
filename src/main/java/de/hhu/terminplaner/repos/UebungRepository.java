package de.hhu.terminplaner.repos;

import de.hhu.terminplaner.domain.uebung.Uebung;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UebungRepository extends CrudRepository<Uebung, Long> {

  List<Uebung> findAll();


}
