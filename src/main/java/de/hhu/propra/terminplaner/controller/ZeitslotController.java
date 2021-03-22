package de.hhu.propra.terminplaner.controller;


import de.hhu.propra.terminplaner.controller.forms.GruppeForm;
import de.hhu.propra.terminplaner.controller.forms.TutorForm;
import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.service.gruppe.GruppeService;
import de.hhu.propra.terminplaner.service.tutor.TutorService;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.util.Arrays;
import java.util.Map;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/zeitslot")
public class ZeitslotController {

  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private GruppeService gruppeService;

  private TutorService tutorService;

  public ZeitslotController(UebungService uebungService, ZeitslotService zeitslotService,
                            TutorService tutorService, GruppeService gruppeService) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.tutorService = tutorService;
    this.gruppeService = gruppeService;
  }


  @Secured("ROLE_ORGA")
  @GetMapping("/{id}/tutoren")
  public String tutoren(@PathVariable("id") Long id,
                        Model model) {
    Uebung uebung = uebungService.findUebungByZeitslotId(id);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    model.addAttribute("tutorform", new TutorForm());
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslot", zeitslot);
    return "tutor/tutorform";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/{id}/addtutor")
  public String placeTutor(@RequestParam("uebungid") Long uebungid, @PathVariable("id") Long id,
                           @ModelAttribute("tutorForm") TutorForm tutorForm,
                           RedirectAttributes redirectAttributes) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    Map<Boolean, String> addTutorZuZeitslot = tutorService
        .checkAnmeldungmodusAndaddTutorZuZeitslot(zeitslot, new Tutor(tutorForm.getName()),
            uebung.getGruppenanmeldung());
    checkResultAndSetupMessage(redirectAttributes, addTutorZuZeitslot);
    return "redirect:/zeitslot/" + id + "/tutoren";
  }


  @GetMapping("/{id}/gruppenform")
  public String getGruppenForm(@PathVariable("id") Long id, Model model) {
    Uebung uebung = uebungService.findUebungByZeitslotId(id);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    model.addAttribute("gruppeform", new GruppeForm());
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslot", zeitslot);
    return "gruppe/gruppeform";
  }

  @PostMapping("/{id}/addgruppe")
  public String placeGruppe(@PathVariable("id") Long id,
                            @ModelAttribute("gruppeform") GruppeForm gruppeForm,
                            RedirectAttributes redirectAttributes) {
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    Gruppe gruppe = new Gruppe(gruppeForm.getGruppeName());
    Arrays.stream(gruppeForm.getStudenten()).filter(x -> !x.getGithubname().isBlank())
        .forEach(gruppe::addStudent);
    Map<Boolean, String> addedGruppeZuZeitslot =
        gruppeService.checkZeitslotZustandAndaddGruppeZuZeitslot(zeitslot, gruppe);
    checkResultAndSetupMessage(redirectAttributes, addedGruppeZuZeitslot);
    return "redirect:/zeitslot/" + id + "/gruppenform";
  }

  private void checkResultAndSetupMessage(RedirectAttributes redirectAttributes,
                                          Map<Boolean, String> addedGruppeZuZeitslot) {
    if (addedGruppeZuZeitslot.containsKey(true)) {
      setMessages(redirectAttributes, null, addedGruppeZuZeitslot.get(true));
    } else {
      setMessages(redirectAttributes, addedGruppeZuZeitslot.get(false), null);
    }
  }

  private void setMessages(RedirectAttributes redirectAttributes, String errorMessage,
                           String successMessage) {
    redirectAttributes.addFlashAttribute("error", errorMessage);
    redirectAttributes.addFlashAttribute("success", successMessage);
  }
}
