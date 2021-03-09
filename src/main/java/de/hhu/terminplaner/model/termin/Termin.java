package de.hhu.terminplaner.model.termin;

import de.hhu.terminplaner.model.gruppe.Gruppe;
import de.hhu.terminplaner.stereotype.AggregateRoot;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AggregateRoot
public class Termin {

  @Id
  private Long id;
  @NonNull
  private LocalDateTime zeitstempel;
  @NonNull
  private Boolean reserviert = false;

  private String wochentag;
  private String uhrzeit;
  private Set<Gruppe> gruppen = new HashSet<>();

  public Termin(@NonNull LocalDateTime zeitstempel) {
    this.zeitstempel = zeitstempel;
  }

  public int size() {
    return gruppen.size();
  }

  public boolean addGruppe(Gruppe gruppe) {
    return this.gruppen.add(gruppe);
  }

}
