package de.hhu.terminplaner.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Termin {

  @NonNull
  private LocalDateTime zeitstempel;
  @NonNull
  private Boolean reserviert = false;

  private String wochentag;
  private String uhrzeit;
  private Gruppe gruppe;

  public Termin(@NonNull LocalDateTime zeitstempel) {
    this.zeitstempel = zeitstempel;
  }
}
