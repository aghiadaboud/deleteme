package de.hhu.terminplaner.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class Test3 {

  @Id
  Long id;

  String s3;

  public Test3(String s3) {
    this.s3 = s3;
  }
}
