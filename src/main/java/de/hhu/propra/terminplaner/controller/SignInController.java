package de.hhu.propra.terminplaner.controller;
//
//import java.util.Map;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//public class SignInController {
//
//
//  @GetMapping("/")
//  public String index(@AuthenticationPrincipal OAuth2User principal, Model model) {
//    model.addAttribute("user",
//        principal != null ? principal.getAttribute("login") : null
//    );
//    return "index";
//  }
//
//  @RequestMapping("/user")
//  public @ResponseBody
//  Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
//    return principal.getAttributes();
//  }
//
//  @GetMapping("/tokeninfo")
//  public @ResponseBody
//  Map<String, Object> tokeninfo(@RegisteredOAuth2AuthorizedClient
//                                    OAuth2AuthorizedClient authorizedClient) {
//    OAuth2AccessToken gitHubAccessToken = authorizedClient.getAccessToken();
//    return Map.of("token", gitHubAccessToken);
//  }
//
//
//}
