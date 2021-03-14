package de.hhu.propra.terminplaner.controller;


import de.hhu.propra.terminplaner.domain.forms.TutorForm;
import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
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
@RequestMapping("/setup")
public class OrganisatorController {

  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private GruppeService gruppeService;

  private TutorService tutorService;
  private StudentService studentService;

  public OrganisatorController(UebungService uebungService, ZeitslotService zeitslotService,
                               TutorService tutorService, GruppeService gruppeService,
                               StudentService studentService) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.tutorService = tutorService;
    this.gruppeService = gruppeService;
    this.studentService = studentService;
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
                              @RequestParam("uhrzeit") String uhrzeit,
                              RedirectAttributes redirectAttributes) {
    Uebung uebung = uebungService.findUebungById(id);
    Map<Boolean, String> addZeitslotzuUebung =
        zeitslotService.checkAnmeldungmodusAndaddZeitslotzuUebung(uebung, datum, uhrzeit);
    checkResultAndSetupMessage(redirectAttributes, addZeitslotzuUebung);
    return "redirect:/setup/uebung/" + id;
  }


  @GetMapping("/uebung/{uebungid}/zeitslot/{id}")
  public String tutoren(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
                        Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    model.addAttribute("tutorform", new TutorForm());
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslot", zeitslot);
    return "organisator/addTutor";
  }

  @PostMapping("/uebung/{uebungid}/zeitslot/{id}")
  public String placeTutor(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
                           @ModelAttribute("tutorForm") TutorForm tutorForm,
                           RedirectAttributes redirectAttributes) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    Map<Boolean, String> addTutorZuZeitslot = tutorService
        .checkAnmeldungmodusAndaddTutorZuZeitslot(zeitslot, new Tutor(tutorForm.getName()),
            uebung.getGruppenanmeldung());
    checkResultAndSetupMessage(redirectAttributes, addTutorZuZeitslot);
    return "redirect:/setup/uebung/" + uebungid + "/zeitslot/" + id;
  }


  @GetMapping("/uebung/{uebungid}/edit")
  public String editUebung(@PathVariable("uebungid") Long uebungid,
                           Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    model.addAttribute("uebung", uebung);
    return "organisator/editUebung";
  }

  @PostMapping("/uebung/{uebungid}/removegruppe")
  public String removeGruppe(@PathVariable("uebungid") Long uebungid,
                             @RequestParam("zeitslotid") Long zeitslotid,
                             @RequestParam("gruppeid") Long gruppeid,
                             RedirectAttributes redirectAttributes) {
    Zeitslot zeitslot = zeitslotService.findZeitslotById(zeitslotid);
    Gruppe gruppe = gruppeService.findGruppeById(gruppeid);
    Map<Boolean, String> deleteGruppe = gruppeService.deleteGruppe(zeitslot, gruppe);
    checkResultAndSetupMessage(redirectAttributes, deleteGruppe);
    return "redirect:/setup/uebung/" + uebungid + "/edit";
  }


  @PostMapping("/uebung/{uebungid}/movestudent")
  public String moveStudent(@PathVariable("uebungid") Long uebungid,
                            @RequestParam("zeitslotidold") Long zeitslotidold,
                            @RequestParam("gruppeidold") Long gruppeidold,
                            @RequestParam("gruppeidnew") Long gruppeidnew,
                            @RequestParam("studentid") Long studentid,
                            RedirectAttributes redirectAttributes) {
    Zeitslot oldZeitslot = zeitslotService.findZeitslotById(zeitslotidold);
    Gruppe oldGruppe = gruppeService.findGruppeById(gruppeidold);
    Zeitslot newZeitslot = zeitslotService.findZeitslotByGruppeId(gruppeidnew);
    Gruppe newGruppe = gruppeService.findGruppeById(gruppeidnew);
    Map<Boolean, String> moved =
        studentService.moveStudent(oldZeitslot, newZeitslot, oldGruppe, newGruppe, studentid);
    checkResultAndSetupMessage(redirectAttributes, moved);
    return "redirect:/setup/uebung/" + uebungid + "/edit";
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
