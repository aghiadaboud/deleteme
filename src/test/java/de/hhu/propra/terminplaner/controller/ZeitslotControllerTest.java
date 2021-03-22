package de.hhu.propra.terminplaner.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import de.hhu.propra.terminplaner.controller.forms.GruppeForm;
import de.hhu.propra.terminplaner.controller.forms.TutorForm;
import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.student.Student;
import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.service.gruppe.GruppeService;
import de.hhu.propra.terminplaner.service.tutor.TutorService;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ZeitslotController.class)
@WithMockUser(username = "someUser", password = "somePassword")
public class ZeitslotControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UebungService uebungService;
  @MockBean
  ZeitslotService zeitslotService;
  @MockBean
  GruppeService gruppeService;
  @MockBean
  TutorService tutorService;

  List<Uebung> uebungen;

  @BeforeEach
  void setup() {
    uebungen = new ArrayList<>();
    Uebung uebung = new Uebung("Uebung-Gruppenanmeldung", true, LocalDate.now(), LocalDate.now());
    uebung.setId(1L);
    Zeitslot zeitslot = new Zeitslot(LocalDate.now(), "08:30");
    zeitslot.setId(1L);
    uebung.addZeitslot(zeitslot);
    uebungen.add(uebung);
    when(uebungService.findUebungById(1L)).thenReturn(uebung);
    when(uebungService.findUebungByZeitslotId(1L)).thenReturn(uebung);
    when(zeitslotService.findZeitslotById(1L)).thenReturn(zeitslot);
    when(uebungService.getAllZeitslotOfUebung(uebung))
        .thenReturn(List.copyOf(uebung.getZeitslots()));
    when(uebungService.findAllUebungen()).thenReturn(uebungen);
  }


  @Test
  @DisplayName("Tutorformular zeigt Übungsinfos")
  void test_1() throws Exception {
    Uebung uebung = uebungen.get(0);
    Zeitslot zeitslot = uebungService.getAllZeitslotOfUebung(uebung).get(0);
    Long id = zeitslot.getId();
    mockMvc.perform(get("/zeitslot/{id}/tutoren", id))
        .andExpect(status().isOk())
        .andExpect(view().name("tutor/tutorform"))
        .andExpect(model().attribute("uebung", uebung))
        .andExpect(model().attribute("zeitslot", zeitslot))
        .andExpect(content().string(allOf(containsString("08:30"),
            containsString("Uebung-Gruppenanmeldung"),
            containsString("Tutor-GithubName"))));

    verify(uebungService).findUebungByZeitslotId(id);
  }

  @Test
  @DisplayName("Tutor zu Zeitslot hinzufügen")
  void test_2() throws Exception {
    Uebung uebung = uebungen.get(0);
    Zeitslot zeitslot = uebungService.getAllZeitslotOfUebung(uebung).get(0);
    Long id = zeitslot.getId();
    TutorForm tutorForm = new TutorForm("someTutor");
    Tutor tutor = new Tutor(tutorForm.getName());
    mockMvc.perform(post("/zeitslot/{id}/addtutor", id)
        .with(csrf())
        .param("uebungid", uebung.getId().toString())
        .flashAttr("tutorForm", tutorForm))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlTemplate("/zeitslot/{id}/tutoren", id));

    verify(uebungService).findUebungById(uebung.getId());
    verify(zeitslotService).findZeitslotById(id);
    verify(tutorService).checkAnmeldungmodusAndaddTutorZuZeitslot(zeitslot, tutor,
        uebung.getGruppenanmeldung());
  }

  @Test
  @DisplayName("Gruppenformular zeigt Übungsinfos")
  void test_3() throws Exception {
    Uebung uebung = uebungen.get(0);
    Zeitslot zeitslot = uebungService.getAllZeitslotOfUebung(uebung).get(0);
    Long id = zeitslot.getId();
    mockMvc.perform(get("/zeitslot/{id}/gruppenform", id))
        .andExpect(status().isOk())
        .andExpect(view().name("gruppe/gruppeform"))
        .andExpect(model().attribute("uebung", uebung))
        .andExpect(model().attribute("zeitslot", zeitslot))
        .andExpect(content().string(allOf(containsString("08:30"),
            containsString("Uebung-Gruppenanmeldung"),
            containsString("gruppeName"))));

    verify(uebungService).findUebungByZeitslotId(id);
  }


  @Test
  @DisplayName("Gruppe zu Zeitslot hinzufügen")
  void test_4() throws Exception {
    Uebung uebung = uebungen.get(0);
    Zeitslot zeitslot = uebungService.getAllZeitslotOfUebung(uebung).get(0);
    Long id = zeitslot.getId();
    GruppeForm gruppeForm = new GruppeForm();
    gruppeForm.setGruppeName("testGruppe");
    gruppeForm
        .setStudenten(new Student[] {new Student("testStudent1"), new Student("testStudent2")});
    Gruppe gruppe = new Gruppe();
    gruppe.setName(gruppeForm.getGruppeName());
    Arrays.stream(gruppeForm.getStudenten()).filter(x -> !x.getGithubname().isBlank())
        .forEach(gruppe::addStudent);

    mockMvc.perform(post("/zeitslot/{id}/addgruppe", id)
        .with(csrf())
        .flashAttr("gruppeform", gruppeForm))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlTemplate("/zeitslot/{id}/gruppenform", id));

    verify(zeitslotService).findZeitslotById(id);
    verify(gruppeService).checkZeitslotZustandAndaddGruppeZuZeitslot(zeitslot, gruppe);
  }

}
