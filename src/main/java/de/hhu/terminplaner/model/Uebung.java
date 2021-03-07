package de.hhu.terminplaner.model;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Uebung {


  @Id
  private Long id;

  private Set<Zeitslot> zeitslots = new HashSet<>();
  @NonNull
  private String name;
  @NonNull
  private Boolean gruppenanmeldung;
  private Anmeldungfrist anmeldungfrist;

  public Uebung(String name, Boolean gruppenanmeldung, Anmeldungfrist anmeldungfrist) {
    this.name = name;
    this.gruppenanmeldung = gruppenanmeldung;
    this.anmeldungfrist = anmeldungfrist;
  }


  public int size() {
    return zeitslots.size();
  }

  public boolean addZeitslot(Zeitslot newZeitslot) {
    return this.zeitslots.add(newZeitslot);
  }

}
