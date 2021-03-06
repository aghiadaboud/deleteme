package de.hhu.terminplaner.model;

import org.springframework.data.annotation.Id;

public class Test2 {
  @Id
  Long id;

  String s2;

  public Test2(String s2) {
    this.s2 = s2;
  }
}
