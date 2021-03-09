package de.hhu.terminplaner.model.gruppe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Repo {

  @NonNull
  private String name;
  @NonNull
  private String link;
}
