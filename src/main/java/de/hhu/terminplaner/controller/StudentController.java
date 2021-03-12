package de.hhu.terminplaner.controller;

import de.hhu.terminplaner.domain.forms.GruppeForm;
import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.student.Student;
import de.hhu.terminplaner.domain.uebung.Uebung;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.service.gruppe.GruppeService;
import de.hhu.terminplaner.service.student.StudentService;
import de.hhu.terminplaner.service.tutor.TutorService;
import de.hhu.terminplaner.service.uebung.UebungService;
import de.hhu.terminplaner.service.zeitslot.ZeitslotService;
import java.util.Arrays;
import java.util.Map;
import org.springframework.context.annotation.Scope;
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
@RequestMapping("/student")
@Scope("session")
public class StudentController {


  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private GruppeService gruppeService;

  private TutorService tutorService;
  private StudentService studentService;

  public StudentController(UebungService uebungService, ZeitslotService zeitslotService,
                           GruppeService gruppeService, TutorService tutorService,
                           StudentService studentService) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.gruppeService = gruppeService;
    this.tutorService = tutorService;
    this.studentService = studentService;
  }

  @GetMapping
  public String uebungen(Model model) {
    model.addAttribute("uebung", new Uebung());
    model.addAttribute("uebungen", uebungService.findAllUebungen());
    return "student/getuebungen";
  }

  @GetMapping("/uebung/{id}")
  public String getzeitslots(@PathVariable("id") Long id, Model model) {
    Uebung uebung = uebungService.findUebungById(id);
    if (!uebung.getGruppenanmeldung()) {
      model.addAttribute("uebung", uebung);
      return "redirect:/student/uebung/" + id + "/oeffeneplaetze";
    }
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslots", uebungService.getAllFreieZeitslotOfUebung(uebung));
    return "student/getzeitslots";
  }

//  @GetMapping("/uebung/{uebungid}/zeitslot/{id}")
//  public String getGruppenUebersicht(@PathVariable("uebungid") Long uebungid,
//                                     @PathVariable("id") Long id,
//                                     Model model) {
//    Uebung uebung = uebungService.findUebungById(uebungid);
//    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
//    model.addAttribute("gruppe", new Gruppe());
//    model.addAttribute("uebung", uebung);
//    model.addAttribute("zeitslot", zeitslot);
//    //model.addAttribute("gruppen", zeitslotService.getFreieGruppenofZeitslot(zeitslot));
//    return "student/addGruppe";
//  }

//  @PostMapping("/uebung/{uebungid}/zeitslot/{id}")
//  public String placeGruppe(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
//                            @RequestParam("gruppename") String gruppename,
//                            @RequestParam("githubname") String githubname) {
//    Uebung uebung = uebungService.findUebungById(uebungid);
//    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
//    Gruppe gruppe = new Gruppe(gruppename);
//    gruppe.addStudent(new Student(githubname));
//    //studentService.addStudentZuGruppe(gruppe, new Student(githubname));
//    gruppeService.addGruppeZuZeitslot(zeitslot, gruppe);
//    return "redirect:/student/uebung/" + uebungid + "/zeitslot/" + id;
//  }

  @GetMapping("/uebung/{uebungid}/zeitslot/{id}")
  public String getGruppenForm(@PathVariable("uebungid") Long uebungid,
                               @PathVariable("id") Long id,
                               Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    model.addAttribute("gruppeform", new GruppeForm());
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslot", zeitslot);
    return "student/addGruppe2";
  }

  @PostMapping("/uebung/{uebungid}/zeitslot/{id}")
  public String placeGruppe(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
                            @ModelAttribute("gruppeform") GruppeForm gruppeForm,
                            RedirectAttributes redirectAttributes) {
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    Gruppe gruppe = new Gruppe(gruppeForm.getGruppeName());
    Arrays.stream(gruppeForm.getStudenten()).filter(x -> !x.getGithubname().isBlank())
        .forEach(gruppe::addStudent);
    Map<Boolean, String> addedGruppeZuZeitslot =
        gruppeService.addGruppeZuZeitslot(zeitslot, gruppe);
    checkResultAndSetupMessage(redirectAttributes, addedGruppeZuZeitslot);
    return "redirect:/student/uebung/" + uebungid + "/zeitslot/" + id;
  }


  @GetMapping("/uebung/{uebungid}/oeffeneplaetze")
  public String getOeffenePlaetze(@PathVariable("uebungid") Long uebungid,
                                  Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    model.addAttribute("uebung", uebung);
    return "student/oeffenePlaetze";
  }

  @PostMapping("/uebung/{uebungid}/oeffeneplaetze")
  public String placeStudent(@PathVariable("uebungid") Long uebungid,
                             @RequestParam("zeitslotid") Long zeitslotid,
                             @RequestParam("gruppeid") Long gruppeid,
                             @RequestParam("studentgithubname") String studentgithubname,
                             RedirectAttributes redirectAttributes) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(zeitslotid);
    Gruppe gruppe = gruppeService.findGruppeById(gruppeid);
    Map<Boolean, String> addedStudentZuGruppe =
        studentService.addStudentZuGruppe(zeitslot, gruppe, new Student(studentgithubname),
            uebung.getGruppenanmeldung());
    checkResultAndSetupMessage(redirectAttributes, addedStudentZuGruppe);
    return "redirect:/student/uebung/" + uebungid + "/oeffeneplaetze";
  }


//  @PostMapping("/uebung/{uebungid}/individual/{zeitslotid}")
//  public String placeStudentIndividual(@PathVariable("uebungid") Long uebungid,
//                                       @PathVariable("zeitslotid") Long zeitslotid,
//                                       @RequestParam("gruppeid") Long gruppeid,
//                                       @RequestParam("studentgithubname") String studentgithubname,
//                                       RedirectAttributes redirectAttributes) {
//    Uebung uebung = uebungService.findUebungById(uebungid);
//    Zeitslot zeitslot = zeitslotService.findZeitslotById(zeitslotid);
//    Gruppe gruppe = gruppeService.findGruppeById(gruppeid);
//    Map<Boolean, String> addedStudentZuGruppe =
//        studentService.addStudentZuGruppe(zeitslot, gruppe, new Student(studentgithubname),
//            uebung.getGruppenanmeldung());
//    checkResultAndSetupMessage(redirectAttributes, addedStudentZuGruppe);
//    return "redirect:/student/uebung/" + uebungid + "/oeffeneplaetze";
//  }


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
