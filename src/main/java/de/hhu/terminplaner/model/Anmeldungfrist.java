package de.hhu.terminplaner.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anmeldungfrist {
  @NonNull
  private LocalDate von;
  @NonNull
  private LocalDate bis;
}
