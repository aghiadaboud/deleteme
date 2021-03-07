package de.hhu.terminplaner.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

  @Id
  private Long id;
  @NonNull
  private String githubname;

  public Student(@NonNull String githubname) {
    this.githubname = githubname;
  }
}
