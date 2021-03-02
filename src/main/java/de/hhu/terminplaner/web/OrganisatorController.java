package de.hhu.terminplaner.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organisator")
public class OrganisatorController {

  @GetMapping("/setup")
  public String setup() {
    return "organisator/setup";
  }
}
