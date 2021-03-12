package de.hhu.terminplaner.service.gruppe;

import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.student.Student;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.repos.GruppeRepository;
import de.hhu.terminplaner.service.zeitslot.ZeitslotService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GruppeService {

  private GruppeRepository gruppeRepository;
  private ZeitslotService zeitslotService;


  public GruppeService(GruppeRepository gruppeRepository, ZeitslotService zeitslotService) {
    this.gruppeRepository = gruppeRepository;
    this.zeitslotService = zeitslotService;
  }

  public List<Gruppe> findAll() {
    return gruppeRepository.findAll();
  }

  public Map<Boolean, String> addGruppeZuZeitslot(Zeitslot zeitslot, Gruppe gruppe) {
    Map<Boolean, String> nachricht = new HashMap<>();
//    gruppe valid??
//    if (!validGruppe(gruppe)) {
//      return nachricht.put(false, "Gruppeinfos sind nicht gültig");
//    }
    if (zeitslot.getKapazitaet() > 0) {
      zeitslot.addGruppe(gruppe);
      zeitslotService.saveZeitslot(zeitslot);
      nachricht.put(true, "Gruppe wurde erfolgreich hinzugefügt");
      return nachricht;
    }
    nachricht.put(false, "Dieser Termin ist reserviert");
    return nachricht;
  }

  public List<Student> getAllGruppenmitglieder(Gruppe gruppe) {
    List<Student> studenten = new ArrayList<>();
    gruppe.getStudenten().forEach(student -> studenten.add(student));
    return studenten;
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
