package de.hhu.propra.terminplaner.repos;

import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GruppeRepository extends CrudRepository<Gruppe, Long> {

  List<Gruppe> findAll();

  @Query("Select * FROM GRUPPE WHERE name = :name")
  Optional<Gruppe> findGruppeByName(@Param("name") String name);

  @Query("Select zeitslot FROM GRUPPE WHERE id = :id")
  Long findZeitslotIdByGruppeId(@Param("id") Long id);
}
