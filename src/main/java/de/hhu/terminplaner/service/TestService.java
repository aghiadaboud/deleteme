package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.Test;
import de.hhu.terminplaner.model.Test2;
import de.hhu.terminplaner.model.Test3;
import de.hhu.terminplaner.repos.TestRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  TestRepository testRepository;

  public TestService(TestRepository testRepository) {
    this.testRepository = testRepository;
  }

  public void createTest(Test2 test2, Test3 test3) {
    Test test = new Test();
    test.setTest2(test2);
    test.addTest3(test3);
    testRepository.save(test);
  }

  public Long createTestohneTest2(String s) {
    Test test = new Test(s);
    testRepository.save(test);
    return test.getId();
  }

  public void addTest3(Test3 test3) {
    Optional<Test> test = testRepository.findById(2L);
    test.get().addTest3(test3);
    testRepository.save(test.get());
  }


}
