package de.hhu.terminplaner.service.zeitslot;

import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.repos.ZeitslotRepository;
import java.time.LocalDate;
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


  public Long createZeitslot(LocalDate datum, String uhrzeit) {
    // termin und tutor validieren
    Zeitslot zeitslot = new Zeitslot(datum, uhrzeit);
    zeitslotRepository.save(zeitslot);
    return zeitslot.getId();
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
