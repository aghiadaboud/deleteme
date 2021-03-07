package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.Test2;
import de.hhu.terminplaner.repos.Test2Repository;
import org.springframework.stereotype.Service;

@Service
public class Test2Service {

  Test2Repository test2Repository;

  public Test2Service(Test2Repository test2Repository) {
    this.test2Repository = test2Repository;
  }

  public Test2 createTest2(String s2) {
    Test2 test2 = new Test2(s2);
    test2Repository.save(test2);
    return test2;
  }

  public Test2 findTest2(Long s2) {
    return test2Repository.findById(s2).get();
  }
}
