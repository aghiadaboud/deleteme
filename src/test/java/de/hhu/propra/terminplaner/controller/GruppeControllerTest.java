package de.hhu.propra.terminplaner.controller;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.student.Student;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.service.gruppe.GruppeService;
import de.hhu.propra.terminplaner.service.student.StudentService;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GruppeController.class)
public class GruppeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UebungService uebungService;
  @MockBean
  ZeitslotService zeitslotService;
  @MockBean
  GruppeService gruppeService;
  @MockBean
  StudentService studentService;
  @MockBean
  OAuth2User oAuth2User;

  List<Uebung> uebungen;

  @BeforeEach
  void setup() {
    uebungen = new ArrayList<>();
    Uebung uebung = new Uebung("Uebung-Gruppenanmeldung", true, LocalDate.now(), LocalDate.now());
    uebung.setId(1L);
    Zeitslot zeitslot = new Zeitslot(LocalDate.now(), "08:30");
    zeitslot.setId(1L);
    Gruppe gruppe = new Gruppe("testGruppe");
    gruppe.setId(1L);
    zeitslot.addGruppe(gruppe);
    uebung.addZeitslot(zeitslot);
    uebungen.add(uebung);
    when(uebungService.findUebungById(1L)).thenReturn(uebung);
    when(zeitslotService.findZeitslotById(1L)).thenReturn(zeitslot);
    when(gruppeService.findGruppeById(1L)).thenReturn(gruppe);
    when(uebungService.getAllZeitslotOfUebung(uebung))
        .thenReturn(List.copyOf(uebung.getZeitslots()));
    when(uebungService.findAllUebungen()).thenReturn(uebungen);
    when(zeitslotService.getAllGruppenOfZeitslot(zeitslot))
        .thenReturn(List.copyOf(zeitslot.getGruppen()));
    when(oAuth2User.getAttribute("login")).thenReturn(
        "someGithubUsername"); //name of the authenticated principal associated with an OAuth 2.0 token.
    when(oAuth2User.getName())
        .thenReturn("someGithubUsername"); //name of the authenticated Principal
  }


  @Test
  @DisplayName("test remove Gruppe")
  @WithMockUser(username = "someUser", password = "somePassword")
  void test_1() throws Exception {
    Uebung uebung = uebungen.get(0);
    Long uebungId = uebung.getId();
    Zeitslot zeitslot = uebungService.getAllZeitslotOfUebung(uebung).get(0);
    Gruppe gruppe = zeitslotService.getAllGruppenOfZeitslot(zeitslot).get(0);
    mockMvc.perform(post("/gruppe/{gruppeid}/removegruppe", gruppe.getId())
        .with(csrf())
        .param("uebungid", uebungId.toString())
        .param("zeitslotid", zeitslot.getId().toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlTemplate("/uebung/{id}/edit", uebungId));

    verify(zeitslotService).findZeitslotById(zeitslot.getId());
    verify(gruppeService).findGruppeById(gruppe.getId());
    verify(gruppeService).deleteGruppe(zeitslot, gruppe);
  }


  @Test
  @DisplayName("Student zu Gruppe hinzufügen")
  void test_2() throws Exception {
    Uebung uebung = uebungen.get(0);
    Long uebungId = uebung.getId();
    Zeitslot zeitslot = uebungService.getAllZeitslotOfUebung(uebung).get(0);
    Gruppe gruppe = zeitslotService.getAllGruppenOfZeitslot(zeitslot).get(0);
    mockMvc.perform(post("/gruppe/{gruppeid}/addstudent", gruppe.getId())
        .with(csrf())
        .with(oauth2Login().oauth2User(oAuth2User))
        .param("uebungid", uebungId.toString())
        .param("zeitslotid", zeitslot.getId().toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlTemplate("/uebung/{id}/oeffeneplaetze", uebungId));

    verify(uebungService).findUebungById(uebungId);
    verify(zeitslotService).findZeitslotById(zeitslot.getId());
    verify(gruppeService).findGruppeById(gruppe.getId());
    verify(studentService)
        .checkAnmeldungmodusAndaddStudentZuGruppe(zeitslot, gruppe,
            new Student("someGithubUsername"),
            uebung.getGruppenanmeldung());
  }


  @Test
  @DisplayName("Student in Gruppe verschieben")
  @WithMockUser(username = "someUser", password = "somePassword")
  void test_3() throws Exception {
    Uebung uebung = uebungen.get(0);
    Long uebungId = uebung.getId();
    Zeitslot oldZeitslot = uebungService.getAllZeitslotOfUebung(uebung).get(0);
    Gruppe oldGruppe = zeitslotService.getAllGruppenOfZeitslot(oldZeitslot).get(0);
    Gruppe newGruppe = new Gruppe("newGruppe");
    newGruppe.setId(2L);
    Zeitslot newZeitslot = new Zeitslot(LocalDate.now(), "12:30");
    newZeitslot.setId(2L);
    Student student = new Student("testGithubUsername");
    student.setId(1L);

    when(zeitslotService.findZeitslotById(2L)).thenReturn(newZeitslot);
    when(gruppeService.findGruppeById(2L)).thenReturn(newGruppe);
    when(zeitslotService.findZeitslotByGruppeId(2L)).thenReturn(newZeitslot);

    mockMvc.perform(post("/gruppe/{gruppeid}/movestudent", oldGruppe.getId())
        .with(csrf())
        .param("uebungid", uebungId.toString())
        .param("zeitslotidold", oldZeitslot.getId().toString())
        .param("gruppeidnew", newGruppe.getId().toString())
        .param("studentid", student.getId().toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlTemplate("/uebung/{id}/edit", uebungId));

    verify(zeitslotService).findZeitslotById(oldZeitslot.getId());
    verify(gruppeService).findGruppeById(oldGruppe.getId());
    verify(zeitslotService).findZeitslotByGruppeId(newGruppe.getId());
    verify(gruppeService).findGruppeById(newGruppe.getId());
    verify(studentService)
        .moveStudent(oldZeitslot, newZeitslot, oldGruppe, newGruppe, student.getId());
  }


}
