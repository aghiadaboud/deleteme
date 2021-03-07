package de.hhu.terminplaner.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class Root {

  @Id
  Long id;

  String r;

  Set<Test> test = new HashSet<>();

  public Root(String r) {
    this.r = r;
  }

  public void addTest(Test newt) {
    this.test.add(newt);
  }
}
