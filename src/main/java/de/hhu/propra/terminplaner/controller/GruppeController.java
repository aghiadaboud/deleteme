package de.hhu.propra.terminplaner.controller;

import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.student.Student;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.service.gruppe.GruppeService;
import de.hhu.propra.terminplaner.service.student.StudentService;
import de.hhu.propra.terminplaner.service.tutor.TutorService;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/gruppe")
public class GruppeController {


  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private GruppeService gruppeService;

  private TutorService tutorService;
  private StudentService studentService;

  public GruppeController(UebungService uebungService, ZeitslotService zeitslotService,
                          TutorService tutorService, GruppeService gruppeService,
                          StudentService studentService) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.tutorService = tutorService;
    this.gruppeService = gruppeService;
    this.studentService = studentService;
  }

  //@Secured("ROLE_Organisator")
  @PostMapping("/{gruppeid}/removegruppe")
  public String removeGruppe(@RequestParam("uebungid") Long uebungid,
                             @RequestParam("zeitslotid") Long zeitslotid,
                             @PathVariable("gruppeid") Long gruppeid,
                             RedirectAttributes redirectAttributes) {
    Zeitslot zeitslot = zeitslotService.findZeitslotById(zeitslotid);
    Gruppe gruppe = gruppeService.findGruppeById(gruppeid);
    Map<Boolean, String> deleteGruppe = gruppeService.deleteGruppe(zeitslot, gruppe);
    checkResultAndSetupMessage(redirectAttributes, deleteGruppe);
    return "redirect:/uebung/" + uebungid + "/edit";
  }


  @PostMapping("/{gruppeid}/addstudent")
  public String placeStudent(@RequestParam("uebungid") Long uebungid,
                             @RequestParam("zeitslotid") Long zeitslotid,
                             @PathVariable("gruppeid") Long gruppeid,
                             @RequestParam("studentgithubname") String studentgithubname,
                             RedirectAttributes redirectAttributes) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(zeitslotid);
    Gruppe gruppe = gruppeService.findGruppeById(gruppeid);
    Map<Boolean, String> addedStudentZuGruppe =
        studentService.checkAnmeldungmodusAndaddStudentZuGruppe(zeitslot, gruppe,
            new Student(studentgithubname),
            uebung.getGruppenanmeldung());
    checkResultAndSetupMessage(redirectAttributes, addedStudentZuGruppe);
    return "redirect:/uebung/" + uebungid + "/oeffeneplaetze";
  }

  //@Secured("ROLE_Organisator")
  @PostMapping("/{gruppeidold}/movestudent")
  public String moveStudent(@RequestParam("uebungid") Long uebungid,
                            @RequestParam("zeitslotidold") Long zeitslotidold,
                            @PathVariable("gruppeidold") Long gruppeidold,
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
    return "redirect:/uebung/" + uebungid + "/edit";
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
