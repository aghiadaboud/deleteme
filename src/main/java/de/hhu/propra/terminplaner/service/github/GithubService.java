package de.hhu.propra.terminplaner.service.github;

import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.student.Student;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.github.JwtHelper;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.kohsuke.github.GHAppInstallation;
import org.kohsuke.github.GHAppInstallationToken;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHPerson;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GithubService {

  /*openssl pkcs8 -topk8 -inform PEM -outform DER -in key.pem -out key.der -nocrypt
   * command zum Erstellen key.der aus dem key.pem*/
  @Value("${appId}")
  private String appIdD;
  @Value("${installationId}")
  private long installation;
  @Value("${organisationName}")
  private String organisationName;

  @Value("${keyLocation}")
  private String keylocation;

  private GitHub gitHub;
  private UebungService uebungService;

  public GithubService(UebungService uebungService) {
    this.uebungService = uebungService;
  }

  private void init() throws Exception {
    String jwt = JwtHelper.createJWT(keylocation, appIdD, 600000);
    GitHub preAuth = new GitHubBuilder().withJwtToken(jwt).build();
    GHAppInstallation appInstallation = preAuth.getApp().getInstallationById(installation);
    GHAppInstallationToken token = appInstallation.createToken().create();
    this.gitHub = new GitHubBuilder().withAppInstallationToken(token.getToken()).build();
  }


  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public void createRepositoryForUebung(Uebung uebung) throws Exception {
    init();
    List<Zeitslot> zeitslots =
        uebungService.getAllZeitslotOfUebung(uebung);
    for (Zeitslot zeitslot : zeitslots) {
      for (Gruppe gruppe : zeitslot.getGruppen()) {
        createReopsitoryForGruppeAndAddStudentenAsCollaborators(uebung.getName(), gruppe,
            gruppe.getStudenten());
      }
    }
  }

  private void createReopsitoryForGruppeAndAddStudentenAsCollaborators(
      @NonNull String uebungName, Gruppe gruppe,
      Set<Student> studenten)
      throws Exception {
    GHOrganization organization = gitHub.getOrganization(organisationName);
    Map<String, GHRepository> organisationRepositories = organization.getRepositories();
    String newRepoName = uebungName + "-" + gruppe.getName();

    List<GHUser> members = organization.listMembers().toList();
    List<String> membersNames =
        members.stream().map(GHPerson::getLogin).collect(Collectors.toList());
    List<Student> membersAndStudents =
        studenten.stream().filter(student -> membersNames.contains(student.getGithubname()))
            .collect(Collectors.toList());

    if (!repoBereitsExistiert(organisationRepositories, newRepoName)
        && !membersAndStudents.isEmpty()) {
      GHRepository repository =
          organization.createRepository(newRepoName).private_(true).create();

      membersAndStudents
          .forEach(student -> {
            try {
              repository.addCollaborators(gitHub.getUser(student.getGithubname()));
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    }
  }

  public void removeStudentVonRepository(Student student, String repositoryName) throws Exception {
    init();
    GHOrganization organization = gitHub.getOrganization(organisationName);
    GHRepository repository = organization.getRepository(repositoryName);
    GHUser user = gitHub.getUser(student.getGithubname());
    if (repository.getCollaboratorNames().contains(student.getGithubname())) {
      repository.removeCollaborators(user);
    }
  }

  public void addStudentZuRepository(Student student, String repositoryName) throws Exception {
    init();
    GHOrganization organization = gitHub.getOrganization(organisationName);
    GHRepository repository = organization.getRepository(repositoryName);
    GHUser user = gitHub.getUser(student.getGithubname());
    if (!repository.getCollaboratorNames().contains(student.getGithubname())) {
      repository.addCollaborators(user);
    }
  }

  private boolean repoBereitsExistiert(Map<String, GHRepository> organisationRepositories,
                                       String newRepoName) {
    return organisationRepositories.keySet().stream().anyMatch(name -> name.equals(newRepoName));
  }


}
