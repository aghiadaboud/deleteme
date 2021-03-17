package de.hhu.propra.terminplaner.service.zeitslot;

import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.repos.GruppeRepository;
import de.hhu.propra.terminplaner.repos.UebungRepository;
import de.hhu.propra.terminplaner.repos.ZeitslotRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ZeitslotService {

  private ZeitslotRepository zeitslotRepository;
  private GruppeRepository gruppeRepository;

  private UebungRepository uebungRepository;

  public ZeitslotService(ZeitslotRepository zeitslotRepository, UebungRepository uebungRepository,
                         GruppeRepository gruppeRepository) {
    this.zeitslotRepository = zeitslotRepository;
    this.uebungRepository = uebungRepository;
    this.gruppeRepository = gruppeRepository;
  }

  public List<Zeitslot> findAll() {
    return zeitslotRepository.findAll();
  }


  public Map<Boolean, String> checkAnmeldungmodusAndaddZeitslotzuUebung(Uebung uebung,
                                                                        LocalDate datum,
                                                                        String uhrzeit) {
    if (uebung.getGruppenanmeldung()) {
      return addZeitslotzuUebungAndGruppeZuZeitslotIfIndividualanmeldung(uebung, datum, uhrzeit,
          Optional.empty());
    } else {
      Gruppe gruppe = new Gruppe("Gruppe-" + datum.toString() + "-" + uhrzeit);
      return addZeitslotzuUebungAndGruppeZuZeitslotIfIndividualanmeldung(uebung, datum, uhrzeit,
          Optional.of(gruppe));
    }
  }


  private Map<Boolean, String> addZeitslotzuUebungAndGruppeZuZeitslotIfIndividualanmeldung(
      Uebung uebung,
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
      uebungRepository.save(uebung);
      //uebungService.saveUebung(uebung);
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

  public void decreaseZeitslotKapazitaetAndUpdateZustand(Zeitslot zeitslot, int platz,
                                                         Optional<Boolean> reserviert) {
    zeitslot.decreaseKapazitaet(platz);
    reserviert.ifPresent(zeitslot::setReserviert);
    zeitslotRepository.save(zeitslot);
  }

  public void increaseKapazitaetAndMakeItAvailable(Zeitslot zeitslot, int platz) {
    zeitslot.increaseZeitslotKapazitaet(1);
    zeitslot.setReserviert(false);
  }

  public Zeitslot findZeitslotByGruppeId(Long gruppeid) {
    Long id = gruppeRepository.findZeitslotIdByGruppeId(gruppeid);
    return findZeitslotById(id);
  }

  public Zeitslot findZeitslotByUebung(Long id, @NonNull LocalDate datum,
                                       @NonNull String uhrzeit) {
    return zeitslotRepository.findZeislotByUebungid(id, datum, uhrzeit);
  }

  public void berechneNeueKapatzitaetAndZustandNachZuteilung(List<Zeitslot> zeitslots) {
    for (Zeitslot zeitslot : zeitslots) {
      zeitslot.setKapazitaet(zeitslot.tutorenAnzahl() - zeitslot.gruppenAnzahl());
      if (zeitslot.getKapazitaet() == 0) {
        zeitslot.setReserviert(true);
      }
      zeitslotRepository.save(zeitslot);
    }
  }
}
