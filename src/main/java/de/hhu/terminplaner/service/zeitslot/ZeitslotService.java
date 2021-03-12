package de.hhu.terminplaner.service.zeitslot;

import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.tutor.Tutor;
import de.hhu.terminplaner.domain.uebung.Uebung;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.repos.ZeitslotRepository;
import de.hhu.terminplaner.service.uebung.UebungService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ZeitslotService {

  private ZeitslotRepository zeitslotRepository;

  private UebungService uebungService;

  public ZeitslotService(ZeitslotRepository zeitslotRepository, UebungService uebungService) {
    this.zeitslotRepository = zeitslotRepository;
    this.uebungService = uebungService;
  }

  public List<Zeitslot> findAll() {
    return zeitslotRepository.findAll();
  }

  public Map<Boolean, String> addZeitslotzuUebung(Uebung uebung, LocalDate datum, String uhrzeit) {
    if (uebung.getGruppenanmeldung()) {
      return addZeitslotzuUebungAndGruppeBeiIndividualanmeldung(uebung, datum, uhrzeit,
          Optional.empty());
    } else {
      Gruppe gruppe = new Gruppe("Gruppe-" + datum.toString() + "-" + uhrzeit);
      return addZeitslotzuUebungAndGruppeBeiIndividualanmeldung(uebung, datum, uhrzeit,
          Optional.of(gruppe));
    }
  }


  private Map<Boolean, String> addZeitslotzuUebungAndGruppeBeiIndividualanmeldung(Uebung uebung,
                                                                                  LocalDate datum,
                                                                                  String uhrzeit,
                                                                                  Optional<Gruppe> gruppe) {
    Map<Boolean, String> nachricht = new HashMap<>();
//    zeitslot valid??liegt nach Anmeldungsfrist?
//        if (!validZeitslot(zeitslot)) {
//      return nachricht.put(false, "Termininfos sind nicht gültig");
//    }
    Zeitslot zeitslot = new Zeitslot(datum, uhrzeit);
    gruppe.ifPresent(zeitslot::addGruppe);
    boolean addedZeitslot = uebung.addZeitslot(zeitslot);
    if (addedZeitslot) {
      uebungService.saveUebung(uebung);
      nachricht.put(true, "Termin wurde erfolgreich hinzugefügt");
      return nachricht;
    }
    nachricht.put(false, "Termin konnte nicht hinzugefügt werden");
    return nachricht;
  }

  public List<Gruppe> getAllGruppenOfZeitslot(Zeitslot zeitslot) {
    List<Gruppe> gruppen = new ArrayList<>();
    gruppen.addAll(zeitslot.getGruppen());
    return gruppen;
  }

  public List<Tutor> getAllTutorOfZeitslot(Zeitslot zeitslot) {
    List<Tutor> tutoren = new ArrayList<>();
    tutoren.addAll(zeitslot.getTutoren());
    return tutoren;
  }

  public Zeitslot findZeitslotById(Long id) {
    Optional<Zeitslot> zeitslot = zeitslotRepository.findById(id);
    if (zeitslot.isPresent()) {
      return zeitslot.get();
    } else {
      throw new NullPointerException("kein Zeitslot für diesen ID vorhanden");
    }
//        return zeitslotRepository.findById(id).orElseThrow(() ->
//                 new ResponseStatusException(NOT_FOUND, "Kein Zeitslot mit id " + id + " vorhanden."));
  }


  public void deleteZeitslot(Long id) {
    zeitslotRepository.deleteById(id);
  }


  public void saveZeitslot(Zeitslot zeitslot) {
    zeitslotRepository.save(zeitslot);
  }

  public int studentenKapazitaetofZeitslotForIndividualanmeldung(Zeitslot zeitslot) {
    return zeitslot.tutorenAnzahl() * 5;
  }
}
