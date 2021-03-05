package de.hhu.terminplaner.web;


import de.hhu.terminplaner.model.Tag;
import de.hhu.terminplaner.model.Tutor;
import de.hhu.terminplaner.model.Zeitslot;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organisator")
public class OrganisatorController {


  @GetMapping("/setup")
  public String setup(Model model) {
    //model.addAttribute("zeitslot", new Zeitslot("", List.of(new Tutor(), new Tutor())));
    model.addAttribute("tag",
        new Tag("", List.of(
            new Zeitslot("z1", List.of(new Tutor(), new Tutor(), new Tutor(), new Tutor())))));
    return "organisator/setup";
  }

  @PostMapping("/setup")
  public String configure(@ModelAttribute("tag") Tag tag) {
    LocalDate localDate = LocalDate.parse(tag.getDatum());
    System.out.println(new SimpleDateFormat("EEEE").format(Date.valueOf(localDate)));
    System.out.println(tag.getZeitslots());
    System.out.println(tag.getDatum());
    System.out.println("////////////////////////////////");
    return "organisator/setup";
  }
}
