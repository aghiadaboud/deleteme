package de.hhu.terminplaner.model;

import java.time.LocalDateTime;
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
