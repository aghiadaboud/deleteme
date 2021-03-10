package de.hhu.terminplaner.domain.uebung;

import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.stereotype.AggregateRoot;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AggregateRoot
public class Uebung {


  @Id
  private Long id;

  private Set<Zeitslot> zeitslots = new HashSet<>();
  @NonNull
  private String name;
  @NonNull
  private Boolean gruppenanmeldung;
  @NonNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate anmeldungfristvon;
  @NonNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate anmeldungfristbis;

  public Uebung(String name, Boolean gruppenanmeldung, @NonNull LocalDate anmeldungfristvon,
                @NonNull LocalDate anmeldungfristbis) {
    this.name = name;
    this.gruppenanmeldung = gruppenanmeldung;
    this.anmeldungfristvon = anmeldungfristvon;
    this.anmeldungfristbis = anmeldungfristbis;
  }


  public int size() {
    return zeitslots.size();
  }

  public boolean addZeitslot(Zeitslot newZeitslot) {
    return this.zeitslots.add(newZeitslot);
  }

}
