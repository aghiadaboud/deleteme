package de.hhu.terminplaner.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organisator")
public class OrganisatorController {


  @GetMapping("/setup")
  public String setup(Model model) {
    return "organisator/setup";
  }

  @PostMapping("/setup")
  public String configure() {
    return "organisator/setup";
  }
}
