package de.hhu.propra.terminplaner.controller;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import de.hhu.propra.terminplaner.service.zuteilung.GruppenZuteilungService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UebungController.class)
@WithMockUser(username = "someUser", password = "somePassword")
//wichtig, sonst kriegt man eine 401 unauthorized exeption from spring security
public class UebungControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UebungService uebungService;
  @MockBean
  ZeitslotService zeitslotService;
  @MockBean
  GruppenZuteilungService gruppenZuteilungService;


  List<Uebung> uebungen;

  @BeforeEach
  void setup() {
    uebungen = new ArrayList<>();
    Uebung emptyUebung = new Uebung();
    Uebung uebungIndividualanmeldung =
        new Uebung("Uebung-Individualnmeldung", false, LocalDate.now(), LocalDate.now());
    uebungIndividualanmeldung.setId(2L);
    Uebung uebung = new Uebung("Uebung-Gruppenanmeldung", true, LocalDate.now(), LocalDate.now());
    uebung.setId(1L);
    Zeitslot zeitslot = new Zeitslot(LocalDate.now(), "08:30");
    zeitslot.setId(1L);
    uebung.addZeitslot(zeitslot);
    uebungen.add(uebung);
    uebungen.add(uebungIndividualanmeldung);
    when(uebungService.createUebung()).thenReturn(emptyUebung);
    when(uebungService.findUebungById(1L)).thenReturn(uebung);
    when(uebungService.findUebungById(2L)).thenReturn(uebungIndividualanmeldung);
    when(uebungService.getAllFreieZeitslotOfUebung(uebung))
        .thenReturn(List.copyOf(uebung.getZeitslots()));
    when(uebungService.ladeVorlage()).thenReturn(Optional.of(uebung));
    when(uebungService.findAllUebungen()).thenReturn(uebungen);
    when(gruppenZuteilungService.passeGruppenAnIndividualanmeldung(2L))
        .thenReturn(uebungIndividualanmeldung);
  }


  @Test
  @DisplayName("test Übungenübersicht")
  void test_1() throws Exception {
    mockMvc.perform(get("/uebung"))
        .andExpect(status().isOk())
        .andExpect(view().name("uebung/uebunguebersicht"))
        .andExpect(model().attribute("uebung", Matchers.notNullValue()))
        .andExpect(model().attribute("uebungen", uebungen))
        .andExpect(content().string(containsString("Uebung-Gruppenanmeldung")));
  }

  @Test
  @DisplayName("ÜbungsVorlage laden")
  void test_2() throws Exception {
    Uebung vorlage = uebungen.get(0);
    mockMvc.perform(get("/uebung/vorlage"))
        .andExpect(status().isOk())
        .andExpect(view().name("uebung/uebungvorlageform"))
        .andExpect(model().attribute("uebung", vorlage))
        .andExpect(content().string(containsString("Uebung-Gruppenanmeldung")));
  }


  @Test
  @DisplayName("Übung von ÜbungsVorlage erstellen")
  void test_3() throws Exception {
    Uebung vorlage = uebungen.get(0);
    mockMvc.perform(post("/uebung/vorlage")
        .with(csrf())
        .param("vorlageid", vorlage.getId().toString())
        .param("uebungname", "newUebung")
        .param("von", LocalDate.now().toString())
        .param("bis", LocalDate.now().toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/uebung/vorlage"));

    verify(uebungService)
        .createUebungFromVorlage(vorlage.getId(), "newUebung", LocalDate.now(),
            LocalDate.now());
  }

  @Test
  @DisplayName("Übung hat ein Zeitslot")
  void test_4() throws Exception {
    Uebung uebung = uebungen.get(0);
    mockMvc.perform(get("/uebung/{id}", uebung.getId()))
        .andExpect(status().isOk())
        .andExpect(view().name("zeitslot/zeitslotuebersicht"))
        .andExpect(model().attribute("uebung", uebung))
        .andExpect(model().attribute("zeitslots", List.copyOf(uebung.getZeitslots())))
        .andExpect(content().string(containsString("08:30")));
  }

  @Test
  @DisplayName("Übung Setup")
  void test_5() throws Exception {
    mockMvc.perform(get("/uebung/setup"))
        .andExpect(status().isOk())
        .andExpect(view().name("uebung/uebungform"))
        .andExpect(model().attribute("uebungen", uebungen));

    verify(uebungService).createUebung();
  }

  @Test
  @DisplayName("neue Übung erstellen")
  void test_6() throws Exception {
    Uebung someUebung = new Uebung("someUuebung", true, LocalDate.now(), LocalDate.now());
    someUebung.setId(999L);
    mockMvc.perform(post("/uebung/setup")
        .with(csrf())
        .flashAttr("uebung", someUebung))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/uebung/setup"));

    verify(uebungService).saveUebung(someUebung);
  }


  @Test
  @DisplayName("Tutoren einer Übung zuteilen")
  void test_7() throws Exception {
    Uebung uebung = uebungen.get(0);
    Long id = uebung.getId();
    mockMvc.perform(post("/uebung/{id}/zuteilen", id)
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlTemplate("/uebung/{uebungid}/edit", id));

    verify(uebungService).bereitsZugeteilt(id);
    verify(gruppenZuteilungService).verteileTutorenAufGruppen(id);
  }

  @Test
  @DisplayName("test Zeitslotsformular")
  void test_8() throws Exception {
    Uebung uebung = uebungen.get(0);
    Long id = uebung.getId();
    mockMvc.perform(get("/uebung/{id}/zeitslotform", id))
        .andExpect(status().isOk())
        .andExpect(view().name("zeitslot/zeitslotform"))
        .andExpect(model().attribute("uebung", uebung))
        .andExpect(model().attribute("zeitslots", uebung.getZeitslots()))
        .andExpect(content().string(containsString("08:30")));
  }

  @Test
  @DisplayName("neuer Termin erstellen")
  void test_9() throws Exception {
    Uebung uebung = uebungen.get(0);
    Long uebungId = uebung.getId();
    Zeitslot someZeitslot = new Zeitslot(LocalDate.now(), "10:30");
    mockMvc.perform(post("/uebung/{id}/addzeitslot", uebungId)
        .with(csrf())
        .param("datum", someZeitslot.getDatum().toString())
        .param("uhrzeit", someZeitslot.getUhrzeit()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlTemplate("/uebung/{id}/zeitslotform", uebungId));

    verify(uebungService).findUebungById(uebungId);
    verify(zeitslotService)
        .checkAnmeldungmodusAndaddZeitslotzuUebung(uebung, someZeitslot.getDatum(),
            someZeitslot.getUhrzeit());
  }


  @Test
  @DisplayName("test edit Übung für Gruppenanmeldung")
  void test_10() throws Exception {
    Uebung uebung = uebungen.get(0);
    Long id = uebung.getId();
    mockMvc.perform(get("/uebung/{uebungid}/edit", id))
        .andExpect(status().isOk())
        .andExpect(view().name("uebung/editUebung"))
        .andExpect(model().attribute("uebung", uebung))
        .andExpect(content().string(containsString("Uebung-Gruppenanmeldung")));

    verify(uebungService).findUebungById(id);
    verify(gruppenZuteilungService, never()).passeGruppenAnIndividualanmeldung(id);
  }

  @Test
  @DisplayName("test edit Übung für Individualanmeldung")
  void test_11() throws Exception {
    Uebung uebung = uebungen.get(1);
    Long id = uebung.getId();
    mockMvc.perform(get("/uebung/{uebungid}/edit", id))
        .andExpect(status().isOk())
        .andExpect(view().name("uebung/editUebung"))
        .andExpect(content().string(containsString("Uebung-Individualnmeldung")));

    verify(uebungService).findUebungById(id);
    verify(gruppenZuteilungService, times(1)).passeGruppenAnIndividualanmeldung(id);
  }

  @Test
  @DisplayName("offene Plätze zeigt Übungsinfos")
  void test_12() throws Exception {
    Uebung uebung = uebungen.get(0);
    Long id = uebung.getId();
    mockMvc.perform(get("/uebung/{uebungid}/oeffeneplaetze", id))
        .andExpect(status().isOk())
        .andExpect(view().name("uebung/offenePlaetze"))
        .andExpect(model().attribute("uebung", uebung))
        .andExpect(content().string(containsString("Uebung-Gruppenanmeldung")));

    verify(uebungService).findUebungById(id);
  }


}
