package de.hhu.propra.terminplaner.security.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RoleLoader {

  private List<String> tutoren = new ArrayList<>();
  private List<String> organisatoren = new ArrayList<>();


  public List<String> getTutoren() {

    Path pathToTutorenFile = Path.of(
        "src/main/resources/tutoren");
    try {
      Stream<String> tutorenLines = Files.lines(pathToTutorenFile);
      tutorenLines.filter(l -> !l.isBlank()).forEach(tutoren::add);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tutoren;
  }

  public List<String> getOrganisatoren() {
    Path pathToOrganisatorenFile = Path.of(
        "src/main/resources/organisatoren");
    try {
      Stream<String> organisatorenLines = Files.lines(pathToOrganisatorenFile);
      organisatorenLines.filter(l -> !l.isBlank()).forEach(organisatoren::add);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return organisatoren;
  }

}
