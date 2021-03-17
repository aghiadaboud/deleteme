package de.hhu.propra.terminplaner.repos;

import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import java.time.LocalDate;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ZeitslotRepository extends CrudRepository<Zeitslot, Long> {

  List<Zeitslot> findAll();

  @Query("Select uebung FROM zeitslot WHERE id = :id")
  Long findUebungIdByZeitslotId(@Param("id") Long id);

  @Query("Select * FROM zeitslot WHERE uebung = :id AND datum = :datum AND uhrzeit = :uhrzeit")
  Zeitslot findZeislotByUebungid(@Param("id") Long id, @NonNull @Param("datum") LocalDate datum,
                                 @NonNull @Param("uhrzeit") String uhrzeit);
}
