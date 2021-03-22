package de.hhu.propra.terminplaner.controller;


import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import de.hhu.propra.terminplaner.service.zuteilung.GruppenZuteilungService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/uebung")
public class UebungController {

  private UebungService uebungService;
  private ZeitslotService zeitslotService;
  private GruppenZuteilungService gruppenZuteilungService;


  public UebungController(UebungService uebungService, ZeitslotService zeitslotService,
                          GruppenZuteilungService gruppenZuteilungService) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.gruppenZuteilungService = gruppenZuteilungService;
  }


  @GetMapping
  public String uebungen(Model model) {
    model.addAttribute("uebung", uebungService.createUebung());
    model.addAttribute("uebungen", uebungService.findAllUebungen());
    return "uebung/uebunguebersicht";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("/vorlage")
  public String vorlage(Model model) {
    Optional<Uebung> uebungVorlage = uebungService.ladeVorlage();
    model.addAttribute("uebung", uebungVorlage.orElse(null));
    return "uebung/uebungvorlageform";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/vorlage")
  public String saveUebungFromVorlage(@RequestParam("vorlageid") Long vorlageid,
                                      @RequestParam("uebungname") String uebungname,
                                      @RequestParam("von") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                          LocalDate von,
                                      @RequestParam("bis") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                          LocalDate bis,
                                      RedirectAttributes redirectAttributes) {
    Map<Boolean, String> created =
        uebungService.createUebungFromVorlage(vorlageid, uebungname, von, bis);
    checkResultAndSetupMessage(redirectAttributes, created);
    return "redirect:/uebung/vorlage";
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

  @Secured({"ROLE_ORGA", "ROLE_TUTOR"})
  @GetMapping("/setup")
  public String uebungForm(Model model) {
    model.addAttribute("uebung", uebungService.createUebung());
    model.addAttribute("uebungen", uebungService.findAllUebungen());
    return "uebung/uebungform";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/setup")
  public String configureUebung(@ModelAttribute("uebung") Uebung uebung,
                                RedirectAttributes redirectAttributes) {
    Map<Boolean, String> created = uebungService.saveUebung(uebung);
    checkResultAndSetupMessage(redirectAttributes, created);
    return "redirect:/uebung/setup";
  }

  @Secured({"ROLE_ORGA", "ROLE_TUTOR"})
  @GetMapping("/{id}/zuteilung")
  public String zuteilung(@PathVariable("id") Long id, Model model) {
    Uebung uebung = uebungService.findUebungById(id);
    model.addAttribute("uebung", uebung);
    return "uebung/zuteilung";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/{id}/zuteilen")
  public String zuteilen(@PathVariable("id") Long id, RedirectAttributes redirectAttributes)
      throws Exception {
    if (uebungService.bereitsZugeteilt(id)) {
      Map<Boolean, String> nachricht = new HashMap<>();
      nachricht.put(false, "Tutoren wurden bereits zugeteilt");
      checkResultAndSetupMessage(redirectAttributes, nachricht);
      return "redirect:/uebung/" + id + "/edit";
    }
    gruppenZuteilungService.verteileTutorenAufGruppen(id);
    return "redirect:/uebung/" + id + "/edit";
  }


  @Secured("ROLE_ORGA")
  @GetMapping("/{id}/zeitslotform")
  public String zeitslotsForm(@PathVariable("id") Long id, Model model) {
    Uebung uebung = uebungService.findUebungById(id);
    model.addAttribute("uebung", uebung);
    model.addAttribute("zeitslots", uebung.getZeitslots());
    return "zeitslot/zeitslotform";
  }


  @Secured("ROLE_ORGA")
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


  @Secured("ROLE_ORGA")
  @GetMapping("/{uebungid}/edit")
  public String editUebung(@PathVariable("uebungid") Long uebungid,
                           Model model) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    if (uebung.getGruppenanmeldung()) {
      model.addAttribute("uebung", uebung);
    } else {
      model.addAttribute("uebung",
          gruppenZuteilungService.passeGruppenAnIndividualanmeldung(uebungid));
    }
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
