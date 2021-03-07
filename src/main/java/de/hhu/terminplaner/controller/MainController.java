package de.hhu.terminplaner.controller;

import de.hhu.terminplaner.model.Test2;
import de.hhu.terminplaner.model.Test3;
import de.hhu.terminplaner.service.RootService;
import de.hhu.terminplaner.service.Test2Service;
import de.hhu.terminplaner.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  TestService testService;
  Test2Service test2Service;

  RootService rootService;

  public MainController(TestService testService, Test2Service test2Service,
                        RootService rootService) {
    this.testService = testService;
    this.test2Service = test2Service;
    this.rootService = rootService;
  }


  @GetMapping("/")
  public String homePage3() {
    Test2 test2 = new Test2("s2");
    Test3 test3 = new Test3("s3");
    testService.createTest(test2, test3);
    System.out.println("/////////////////////////////////////////////////////////");
    System.out.println(test2Service.findTest2(1L));
    System.out.println("/////////////////////////////////////////////////////////");


    return "test";
  }

  @GetMapping("/later2")
  public String homePage33() {
    Test2 test2 = new Test2("s22");
    Test3 test3 = new Test3("s33");
    testService.createTest(test2, test3);


    return "test";
  }

}
