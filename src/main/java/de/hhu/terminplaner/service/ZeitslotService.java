package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.Termin;
import de.hhu.terminplaner.model.Tutor;
import de.hhu.terminplaner.model.Zeitslot;
import de.hhu.terminplaner.repos.ZeitslotRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ZeitslotService {

  private ZeitslotRepository zeitslotRepository;

  public ZeitslotService(ZeitslotRepository zeitslotRepository) {
    this.zeitslotRepository = zeitslotRepository;
  }

  public List<Zeitslot> findAll() {
    return zeitslotRepository.findAll();
  }


  public void createZeitslot(Termin termin, Tutor tutor) {
    Zeitslot zeitslot = new Zeitslot(termin, tutor);
    zeitslotRepository.save(zeitslot);
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

}
