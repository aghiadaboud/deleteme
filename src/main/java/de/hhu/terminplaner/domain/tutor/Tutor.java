package de.hhu.terminplaner.domain.tutor;


import de.hhu.terminplaner.stereotype.AggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@AggregateRoot
public class Tutor {

  @Id
  private Long id;
  @NonNull
  private String githubname;

  public Tutor(@NonNull String githubname) {
    this.githubname = githubname;
  }
}
