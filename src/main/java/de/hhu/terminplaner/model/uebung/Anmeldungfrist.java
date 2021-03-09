package de.hhu.terminplaner.model.uebung;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Anmeldungfrist {
  @NonNull
  private LocalDate von;
  @NonNull
  private LocalDate bis;
}
