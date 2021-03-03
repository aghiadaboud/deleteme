package de.hhu.terminplaner.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zeitslot {

  private String uhrzeit;
  private List<Tutor> tutoren = new ArrayList<>();
}
