package de.hhu.propra.terminplaner.security.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private RoleLoader roleLoader = new RoleLoader();

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests(a -> a
        .antMatchers("/", "/error").permitAll()
        .anyRequest().authenticated())
        .exceptionHandling(
            e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .logout(l -> l.logoutSuccessUrl("/").permitAll())
        .oauth2Login()
        .userInfoEndpoint()
        .userService(initOAuth2UserService());
    ;
  }


  private OAuth2UserService<OAuth2UserRequest, OAuth2User> initOAuth2UserService() {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService =
        new DefaultOAuth2UserService();

    return oAuth2UserRequest -> {
      OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

      Map<String, Object> attributes = oAuth2User.getAttributes(); //keep existing attributes

      String attributeNameKey = oAuth2UserRequest.getClientRegistration()
          .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

      Set<GrantedAuthority> authorities = new HashSet<>();

      // Standard USER Role hinzufügen


      //Read lists from some config. hardcoded only for example purposes.
      List<String> orgaList = roleLoader.getOrganisatoren();
      List<String> tutorInnenList = roleLoader.getTutoren();


      // Prüfen auf Rollen
      if (orgaList.contains(attributes.get("login").toString())) {
        authorities.add(new SimpleGrantedAuthority("ROLE_ORGA"));
      } else if (tutorInnenList.contains(attributes.get("login").toString())) {
        authorities.add(new SimpleGrantedAuthority("ROLE_TUTOR"));
      } else {
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
      }

      return new DefaultOAuth2User(authorities, attributes, attributeNameKey);
    };
  }
}
