package de.hhu.terminplaner.model;

import org.springframework.data.annotation.Id;


public class Test {
  @Id
  Long id;

  String s;

  Test2 test2;

  public Test(String s, Test2 test2) {
    this.s = s;
    this.test2 = test2;
  }

  public Test(String s) {
    this.s = s;
  }

  public void setTest2(Test2 test2) {
    this.test2 = test2;
    System.out.println("///////////////////////////////////");
  }

  public Long getId() {
    return id;
  }
}
