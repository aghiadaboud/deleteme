package de.hhu.terminplaner.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repo {

  @NonNull
  private String name;
  @NonNull
  private String link;
}
