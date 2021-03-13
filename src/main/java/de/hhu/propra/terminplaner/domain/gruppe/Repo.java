package de.hhu.propra.terminplaner.domain.gruppe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
class Repo {

  @NonNull
  private String name;
  @NonNull
  private String link;
}
