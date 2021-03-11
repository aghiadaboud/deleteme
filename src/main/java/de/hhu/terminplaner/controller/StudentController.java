package de.hhu.terminplaner.controller;

import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.student.Student;
import de.hhu.terminplaner.domain.uebung.Uebung;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.service.gruppe.GruppeService;
import de.hhu.terminplaner.service.student.StudentService;
import de.hhu.terminplaner.service.tutor.TutorService;
import de.hhu.terminplaner.service.uebung.UebungService;
import de.hhu.terminplaner.service.zeitslot.ZeitslotService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/student")
@Scope("session")
public class StudentController {


  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private GruppeService gruppeService;

  private TutorService tutorService;
  private StudentService studentService;

  public StudentController(UebungService uebungService, ZeitslotService zeitslotService
      , GruppeService gruppeService, TutorService tutorService, StudentService studentService) {
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
  public String zeitslots(@PathVariable("id") Long id, Model model) {
    Uebung uebung = uebungService.findUebungById(id);
    model.addAttribute("uebung", uebung);
    //model.addAttribute("zeitslots", uebung.getZeitslots());
    return "student/getzeitslots";
  }

  @GetMapping("/uebung/{uebungid}/zeitslot/{id}")
  public String tutoren(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
                        Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    model.addAttribute("gruppe", new Gruppe());
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslot", zeitslot);
    return "student/addGruppe";
  }

  @PostMapping("/uebung/{uebungid}/zeitslot/{id}")
  public String placeTutor(@PathVariable("uebungid") Long uebungid, @PathVariable("id") Long id,
                           @RequestParam("gruppename") String gruppename,
                           @RequestParam("githubname") String githubname) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    Gruppe gruppe = new Gruppe(gruppename);
    gruppe.addStudent(new Student(githubname));
    //studentService.addStudentZuGruppe(gruppe, new Student(githubname));
    gruppeService.addGruppeZuZeitslot(zeitslot, gruppe);
    return "redirect:/student/uebung/" + uebungid + "/zeitslot/" + id;
  }

  @PostMapping("/uebung/{uebungid}/zeitslot/{zeitslotid}/gruppe/{id}")
  public String placeTutor(@PathVariable("uebungid") Long uebungid,
                           @PathVariable("zeitslotid") Long zeitslotid,
                           @PathVariable("id") Long id,
                           @RequestParam("studentgithubname") String studentgithubname) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Zeitslot zeitslot = zeitslotService.findZeitslotById(id);
    Gruppe gruppe = gruppeService.findGruppeById(id);
    studentService.addStudentZuGruppe(gruppe, new Student(studentgithubname));
    return "redirect:/student/uebung/" + uebungid + "/zeitslot/" + zeitslotid;
  }


}
