package de.hhu.propra.terminplaner.controller;


import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.service.gruppe.GruppeService;
import de.hhu.propra.terminplaner.service.student.StudentService;
import de.hhu.propra.terminplaner.service.tutor.TutorService;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/uebung")
public class UebungController {

  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private GruppeService gruppeService;

  private TutorService tutorService;
  private StudentService studentService;

  public UebungController(UebungService uebungService, ZeitslotService zeitslotService,
                          TutorService tutorService, GruppeService gruppeService,
                          StudentService studentService) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.tutorService = tutorService;
    this.gruppeService = gruppeService;
    this.studentService = studentService;
  }


  @GetMapping
  public String uebungen(Model model) {
    model.addAttribute("uebung", new Uebung());
    model.addAttribute("uebungen", uebungService.findAllUebungen());
    return "uebung/uebunguebersicht";
  }


  @GetMapping("/{id}")
  public String getzeitslots(@PathVariable("id") Long id, Model model) {
    Uebung uebung = uebungService.findUebungById(id);
    if (!uebung.getGruppenanmeldung()) {
      model.addAttribute("uebung", uebung);
      return "redirect:/uebung/" + id + "/oeffeneplaetze";
    }
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslots", uebungService.getAllFreieZeitslotOfUebung(uebung));
    return "zeitslot/zeitslotuebersicht";
  }

  //@Secured("ROLE_Organisator")
  @GetMapping("/setup")
  public String uebungForm(Model model) {
    model.addAttribute("uebung", new Uebung());
    model.addAttribute("uebungen", uebungService.findAllUebungen());
    return "uebung/uebungform";
  }

  //@Secured("ROLE_Organisator")
  @PostMapping("/setup")
  public String configureUebung(@ModelAttribute("uebung") Uebung uebung) {
    //hier muss eine createUebung methode aufgerufen, die checkt ob uebung valid, unf micht direkt saveUebung
    Uebung newUebung = uebungService.saveUebung(uebung);
    return "redirect:/uebung/setup";
  }


  //@Secured("ROLE_Organisator")
  @GetMapping("/{id}/zeitslotform")
  public String zeitslotsForm(@PathVariable("id") Long id, Model model) {
    Uebung uebung = uebungService.findUebungById(id);
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslots", uebung.getZeitslots());
    return "zeitslot/zeitslotform";
  }


  //@Secured("ROLE_Organisator")
  @PostMapping("/{id}/addzeitslot")
  public String placeZeitslot(@PathVariable("id") Long id,
                              @RequestParam("datum") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                  LocalDate datum,
                              @RequestParam("uhrzeit") String uhrzeit,
                              RedirectAttributes redirectAttributes) {
    Uebung uebung = uebungService.findUebungById(id);
    Map<Boolean, String> addZeitslotzuUebung =
        zeitslotService.checkAnmeldungmodusAndaddZeitslotzuUebung(uebung, datum, uhrzeit);
    checkResultAndSetupMessage(redirectAttributes, addZeitslotzuUebung);
    return "redirect:/uebung/" + id + "/zeitslotform";
  }


  //@Secured("ROLE_Organisator")
  @GetMapping("/{uebungid}/edit")
  public String editUebung(@PathVariable("uebungid") Long uebungid,
                           Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    model.addAttribute("uebung", uebung);
    return "uebung/editUebung";
  }


  @GetMapping("/{uebungid}/oeffeneplaetze")
  public String getOeffenePlaetze(@PathVariable("uebungid") Long uebungid,
                                  Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    model.addAttribute("uebung", uebung);
    return "uebung/offenePlaetze";
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
