package de.hhu.propra.terminplaner.domain.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TutorForm {

  private String name;

  public TutorForm(String name) {
    this.name = name;
  }
}
