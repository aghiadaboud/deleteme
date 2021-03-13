package de.hhu.propra.terminplaner.domain.gruppe;


import de.hhu.propra.terminplaner.domain.student.Student;
import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.stereotype.AggregateRoot;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Data
@NoArgsConstructor
@AggregateRoot
public class Gruppe {

  @Id
  private Long id;
  @NonNull
  private String name;
  private Set<Student> studenten = new HashSet<>();
  private Repo repo;
  private Tutor tutor;
  @Transient
  private Integer min;
  @Transient
  private Integer max;

  public Gruppe(@NonNull String name) {
    this.name = name;
  }

  public int size() {
    return studenten.size();
  }

  public boolean addStudent(Student student) {
    return this.studenten.add(student);
  }
}
