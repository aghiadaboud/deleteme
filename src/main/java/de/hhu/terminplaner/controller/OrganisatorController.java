package de.hhu.terminplaner.controller;


import de.hhu.terminplaner.domain.tutor.Tutor;
import de.hhu.terminplaner.domain.uebung.Uebung;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.service.tutor.TutorService;
import de.hhu.terminplaner.service.uebung.UebungService;
import de.hhu.terminplaner.service.zeitslot.ZeitslotService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/setup")
public class OrganisatorController {

  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private TutorService tutorService;

  public OrganisatorController(UebungService uebungService, ZeitslotService zeitslotService,
                               TutorService tutorService) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.tutorService = tutorService;
  }

  @GetMapping
  public String setup(Model model) {
    model.addAttribute("uebung", new Uebung());
    model.addAttribute("uebungen", uebungService.findAllUebungen());
    return "organisator/addUebung";
  }

  @PostMapping
  public String configure(@ModelAttribute("uebung") Uebung uebung) {
    //hier muss eine createUebung methode aufgerufen, die checkt ob uebung valid, unf micht direkt saveUebung
    Uebung newUebung = uebungService.saveUebung(uebung);
    return "redirect:/setup";
  }

  @GetMapping("/uebung/{id}")
  public String zeitslots(@PathVariable("id") Long id, Model model) {
    Uebung uebung = uebungService.findUebungById(id);
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslots", uebung.getZeitslots());
    return "organisator/addZeitslot";
  }

  @PostMapping("/uebung/{id}")
  public String placeZeitslot(@PathVariable("id") Long id,
                              @RequestParam("datum") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                  LocalDate datum,
                              @RequestParam("uhrzeit") String uhrzeit) {

    Uebung uebung = uebungService.findUebungById(id);
    zeitslotService.addZeitslotzuUebung(uebung, datum, uhrzeit);
    return "redirect:/setup/uebung/" + id;
  }

  @GetMapping("/uebung/{uebungid}/zeitslot/{id}")
  public String tutoren(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
                        Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslot", zeitslot);
    return "organisator/addTutor";
  }

  @PostMapping("/uebung/{uebungid}/zeitslot/{id}")
  public String placeTutor(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
                           @RequestParam("githubname") String githubname, Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    tutorService.addTutorZuZeitslot(zeitslot, new Tutor(githubname));
    return "redirect:/setup/uebung/" + uebungid + "/zeitslot/" + id;
  }

//  @GetMapping("/uebung/{uebungid}/zeitslot/{id}")
//  public String tutoren(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
//                        Model model) {
//    Uebung uebung = uebungService.findUebungById(uebungid);
//    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
//    model.addAttribute("tutorform", new TutorForm());
//    model.addAttribute("uebung", uebung);
//    model.addAttribute("zeitslot", zeitslot);
//    return "organisator/addTutor";
//  }
//
//  @PostMapping("/uebung/{uebungid}/zeitslot/{id}")
//  public String placeTutor(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
//                           @ModelAttribute("tutor") TutorForm tutorForm) {
//    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
//    tutorService.addTutorZuZeitslot(zeitslot, new Tutor(tutorForm.getName()));
//    return "redirect:/setup/uebung/" + uebungid + "/zeitslot/" + id;
//  }

}
