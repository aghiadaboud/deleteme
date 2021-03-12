package de.hhu.terminplaner.domain.forms;

import de.hhu.terminplaner.domain.student.Student;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GruppeForm {

  private Student[] studenten = new Student[5];
  private String gruppeName;
}
