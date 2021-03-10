package de.hhu.terminplaner.domain.uebung;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
class Organisator {

  @Id
  private Long id;
  @NonNull
  private String githubname;

  public Organisator(@NonNull String githubname) {
    this.githubname = githubname;
  }
}
