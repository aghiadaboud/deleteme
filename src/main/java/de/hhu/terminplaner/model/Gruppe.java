package de.hhu.terminplaner.model;


import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gruppe {

  @Id
  private Long id;
  @NonNull
  private String name;
  private Set<Student> studenden = new HashSet<>();
  private Repo repo;
  private Integer min;
  private Integer max;

  public Gruppe(@NonNull String name) {
    this.name = name;
  }

  public int size() {
    return studenden.size();
  }

  public boolean addStudent(Student student) {
    return this.studenden.add(student);
  }
}
