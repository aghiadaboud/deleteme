package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.Test;
import de.hhu.terminplaner.model.Test2;
import de.hhu.terminplaner.repos.TestRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  TestRepository testRepository;

  public TestService(TestRepository testRepository) {
    this.testRepository = testRepository;
  }

  public void createTest(String s, Test2 test2) {
    Test test = new Test(s, test2);
    testRepository.save(test);
  }

  public Long createTestohneTest2(String s) {
    Test test = new Test(s);
    testRepository.save(test);
    return test.getId();
  }

  public void addTest2(Long id, Test2 test2) {
    Optional<Test> test = testRepository.findById(id);
    test.get().setTest2(test2);
    testRepository.save(test.get());
  }


}
