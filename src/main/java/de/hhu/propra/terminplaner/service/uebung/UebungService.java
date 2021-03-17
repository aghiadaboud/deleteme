package de.hhu.propra.terminplaner.service.uebung;

import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.repos.UebungRepository;
import de.hhu.propra.terminplaner.repos.ZeitslotRepository;
import de.hhu.propra.terminplaner.service.tutor.TutorService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UebungService {

  private UebungRepository uebungRepository;

  private ZeitslotRepository zeitslotRepository;

  private ZeitslotService zeitslotService;
  private TutorService tutorService;

  public UebungService(UebungRepository uebungRepository, ZeitslotRepository zeitslotRepository,
                       ZeitslotService zeitslotService, TutorService tutorService) {
    this.uebungRepository = uebungRepository;
    this.zeitslotRepository = zeitslotRepository;
    this.zeitslotService = zeitslotService;
    this.tutorService = tutorService;
  }

  public List<Uebung> findAllUebungen() {
    return uebungRepository.findAll();
  }

  public Uebung findUebungById(Long uebungid) {
    Optional<Uebung> uebung = uebungRepository.findById(uebungid);

    if (uebung.isPresent()) {
      return uebung.get();
    } else {
      throw new NullPointerException("keine Uebung für diese ID vorhanden");
    }
//        return uebungRepository.findById(uebungid).orElseThrow(() ->
//                new ResponseStatusException(NOT_FOUND, "Keine Uebung mit id " + uebungid + " vorhanden."));
  }

  public Long createUebung(String name, Boolean gruppenanmeldung, LocalDate von, LocalDate bis) {
    Uebung uebung = new Uebung(name, gruppenanmeldung, von, bis);
    uebungRepository.save(uebung);
    return uebung.getId();
  }


  public List<Zeitslot> getAllZeitslotOfUebung(Uebung uebung) {
    List<Zeitslot> zeitslots = new ArrayList<>();
    zeitslots.addAll(uebung.getZeitslots());
    return zeitslots;
  }

  public List<Zeitslot> getAllFreieZeitslotOfUebung(Uebung uebung) {
    List<Zeitslot> zeitslots = getAllZeitslotOfUebung(uebung);
    List<Zeitslot> freieZeitslots =
        zeitslots.stream()
            .filter(zeitslot -> !zeitslot.getReserviert())
            .collect(Collectors.toList());
    return freieZeitslots;
  }


  public Optional<List<Zeitslot>> getAllReservierteZeitslots(Uebung uebung) {
    List<Zeitslot> zeitslots = getAllZeitslotOfUebung(uebung);
    List<Zeitslot> reservierteZeitslots =
        zeitslots.stream()
            .filter(zeitslot -> zeitslot.getReserviert())
            .collect(Collectors.toList());
    if (reservierteZeitslots.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(reservierteZeitslots);
  }


  public void deleteZeitslot(Long uebungid, @NonNull Zeitslot zeitslot)
      throws NullPointerException {
    Uebung uebung = findUebungById(uebungid);
    uebung.getZeitslots().remove(zeitslot);
    uebungRepository.save(uebung);
  }

  public Uebung saveUebung(Uebung uebung) {
    uebungRepository.save(uebung);
    return uebung;
  }

  public Uebung findUebungByZeitslotId(Long id) {
    Long uebungId = zeitslotRepository.findUebungIdByZeitslotId(id);
    return findUebungById(uebungId);
  }

  public Optional<Uebung> ladeVorlage() {
    List<Uebung> allUebungen = uebungRepository.findAll();
    List<LocalDate> daten =
        allUebungen.stream().map(Uebung::getAnmeldungfristbis).collect(Collectors.toList());
    List<LocalDate> sortedDaten =
        daten.stream().sorted(LocalDate::compareTo).collect(Collectors.toList());
    if (!sortedDaten.isEmpty()) {
      LocalDate letztesUebungDatum = sortedDaten.get(sortedDaten.size() - 1);
      return allUebungen.stream()
          .filter(uebung -> uebung.getAnmeldungfristbis().equals(letztesUebungDatum)).findAny();
    }
    return Optional.empty();
  }


  public Map<Boolean, String> createUebungFromVorlage(Long vorlageid, String newUebungname,
                                                      LocalDate von, LocalDate bis) {
    Map<Boolean, String> nachricht = new HashMap<>();
    Uebung vorlage = findUebungById(vorlageid);
    Uebung newUebung = new Uebung(newUebungname, vorlage.getGruppenanmeldung(), von, bis);
    for (Zeitslot zeitslot : vorlage.getZeitslots()) {
      zeitslotService.checkAnmeldungmodusAndaddZeitslotzuUebung(newUebung, zeitslot.getDatum(),
          zeitslot.getUhrzeit());
      Zeitslot newZeitslot = zeitslotService
          .findZeitslotByUebung(newUebung.getId(), zeitslot.getDatum(), zeitslot.getUhrzeit());
      zeitslot.getTutoren().forEach(
          tutor -> tutorService.checkAnmeldungmodusAndaddTutorZuZeitslot(newZeitslot, new Tutor(
              tutor.getGithubname()), newUebung.getGruppenanmeldung()));
      zeitslotRepository.save(newZeitslot);
    }
    //uebungRepository.save(newUebung);
    nachricht.put(true,
        "Übung wurde erstellt. Gehen Sie zur Uebung-Setup Seite um die neue Übung zu sehen!");
    return nachricht;
  }
}
