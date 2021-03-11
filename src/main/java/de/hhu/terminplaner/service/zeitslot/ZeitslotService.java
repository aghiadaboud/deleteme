package de.hhu.terminplaner.service.zeitslot;

import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.tutor.Tutor;
import de.hhu.terminplaner.domain.uebung.Uebung;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.repos.ZeitslotRepository;
import de.hhu.terminplaner.service.uebung.UebungService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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


  public void addZeitslotzuUebung(Uebung uebung, LocalDate datum, String uhrzeit) {
    Zeitslot zeitslot = new Zeitslot(datum, uhrzeit);
    //zeitslot valid??
    if (uebung.addZeitslot(zeitslot)) {
      uebungService.saveUebung(uebung);
    }
  }

  public List<Gruppe> getAllGruppenOfZeitslot(Zeitslot zeitslot) {
    List<Gruppe> gruppen = new ArrayList<>();
    zeitslot.getGruppen().forEach(gruppe -> gruppen.add(gruppe));
    return gruppen;
  }

  public List<Tutor> getAllTutorOfZeitslot(Zeitslot zeitslot) {
    List<Tutor> tutoren = new ArrayList<>();
    zeitslot.getTutoren().forEach(tutor -> tutoren.add(tutor));
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
}
