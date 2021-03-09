package de.hhu.terminplaner.model.gruppe;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Student {

  @NonNull
  private String githubname;

  public Student(@NonNull String githubname) {
    this.githubname = githubname;
  }
}
