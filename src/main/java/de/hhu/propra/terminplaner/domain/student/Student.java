package de.hhu.propra.terminplaner.domain.student;


import de.hhu.propra.terminplaner.stereotype.AggregateRoot;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AggregateRoot
public class Student {

  @Id
  private Long id;
  @NonNull
  private String githubname;

  public Student(@NonNull String githubname) {
    this.githubname = githubname;
  }
}
