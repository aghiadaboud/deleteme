package de.hhu.terminplaner.service.gruppe;

import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.student.Student;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.repos.GruppeRepository;
import de.hhu.terminplaner.service.zeitslot.ZeitslotService;
import java.util.ArrayList;
import java.util.List;
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

  public void addGruppeZuZeitslot(Zeitslot zeitslot, Gruppe gruppe) {
    //gruppe valid??
    if (zeitslot.addGruppe(gruppe)) {
      zeitslotService.saveZeitslot(zeitslot);
    }
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
