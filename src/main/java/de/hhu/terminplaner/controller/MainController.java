package de.hhu.terminplaner.controller;

import de.hhu.terminplaner.model.Test2;
import de.hhu.terminplaner.service.Test2Service;
import de.hhu.terminplaner.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  TestService testService;
  Test2Service test2Service;

  public MainController(TestService testService, Test2Service test2Service) {
    this.testService = testService;
    this.test2Service = test2Service;
  }

  @GetMapping("/")
  public String homePage() {
    Test2 test2 = test2Service.createTest2("test2");
    Long test1id = testService.createTestohneTest2("test1");
    System.out.println(test1id);
    testService.addTest2(test1id, test2);
    return "test";
  }

}
