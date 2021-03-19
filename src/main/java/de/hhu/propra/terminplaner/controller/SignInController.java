package de.hhu.propra.terminplaner.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInController {


  @GetMapping("/")
  public String index(@AuthenticationPrincipal OAuth2User principal, Model model) {
    model.addAttribute("user",
        principal != null ? principal.getAttribute("login") : null
    );
    return "index";
  }

}
