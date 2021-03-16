package de.hhu.propra.terminplaner.service.gruppe;

import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.student.Student;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.repos.GruppeRepository;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class GruppeService {

  private GruppeRepository gruppeRepository;
  private ZeitslotService zeitslotService;


  public GruppeService(GruppeRepository gruppeRepository, ZeitslotService zeitslotService) {
    this.gruppeRepository = gruppeRepository;
    this.zeitslotService = zeitslotService;
  }

  public boolean checkStundentIsInGroup(Gruppe gruppe, String githubName) {
    Optional<Student> studentFound = gruppe.getStudenten()
        .stream()
        .filter(student -> student.getGithubname()
            .equals(githubName))
        .findAny();
    if (studentFound.isPresent()) {
      return true;
    }
    return false;
  }

  public List<Gruppe> findAll() {
    return gruppeRepository.findAll();
  }


  public Map<Boolean, String> checkZeitslotZustandAndaddGruppeZuZeitslot(Zeitslot zeitslot,
                                                                         Gruppe gruppe) {
    Map<Boolean, String> nachricht = new HashMap<>();
    int zeitslotKapazitaet = zeitslot.getKapazitaet();
    if (zeitslotKapazitaet == 0) {
      nachricht.put(false, "Dieser Termin ist reserviert");
      return nachricht;
    } else if (zeitslotKapazitaet == 1) {
      return addGruppeZuZeitslotAndDecreaseKapazitaet(zeitslot, gruppe, true);
    } else {
      return addGruppeZuZeitslotAndDecreaseKapazitaet(zeitslot, gruppe, false);
    }
  }


  private Map<Boolean, String> addGruppeZuZeitslotAndDecreaseKapazitaet(Zeitslot zeitslot,
                                                                        Gruppe gruppe,
                                                                        boolean reserviert) {
    Map<Boolean, String> nachricht = new HashMap<>();
    //    gruppe valid??
    //    if (!validGruppe(gruppe)) {
    //      return nachricht.put(false, "Gruppeinfos sind nicht gültig");
    //    }
    zeitslot.addGruppe(gruppe);
//    zeitslot.decreaseKapazitaet(1);
//    zeitslot.setReserviert(reserviert);
//    zeitslotService.saveZeitslot(zeitslot);
    zeitslotService
        .decreaseZeitslotKapazitaetAndUpdateZustand(zeitslot, 1, Optional.of(reserviert));
    nachricht.put(true, "Gruppe erfolgreich hinzugefügt");
    return nachricht;
  }

  public List<Student> getAllGruppenmitglieder(Gruppe gruppe) {
    List<Student> studenten = new ArrayList<>();
    gruppe.getStudenten().forEach(student -> studenten.add(student));
    return studenten;
  }

  public Map<Boolean, String> deleteGruppe(Zeitslot zeitslot, Gruppe gruppe) {
    Map<Boolean, String> nachricht = new HashMap<>();
    Set<Gruppe> gruppen = zeitslot.getGruppen();
    boolean deleted = gruppen.remove(gruppe);
    if (deleted) {
      zeitslotService.increaseKapazitaetAndMakeItAvailable(zeitslot, 1);
      zeitslotService.saveZeitslot(zeitslot);
      nachricht.put(true, "Gruppe wurde gelöscht");
      return nachricht;
    }
    nachricht.put(true, "Gruppe konnte nicht gelöscht werden");
    return nachricht;
  }

  public int countStudenten(Long id) {
    Gruppe gruppe = findGruppeById(id);
    return gruppe.size();
  }

  public Gruppe findGruppeById(Long id) {
    Optional<Gruppe> gruppe = gruppeRepository.findById(id);
    if (gruppe.isPresent()) {
      return gruppe.get();
    } else {
      throw new NullPointerException("keine Gruppe für diesen ID vorhanden");
    }
    //        return gruppeRepository.findById(id).orElseThrow(() ->
//                 new ResponseStatusException(NOT_FOUND, "Keine Gruppe mit id " + id + " vorhanden."));

  }

  public void saveGruppe(Gruppe gruppe) {
    gruppeRepository.save(gruppe);
  }
}
