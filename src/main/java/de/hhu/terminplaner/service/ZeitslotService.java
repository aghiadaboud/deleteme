package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.Gruppe;
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

  public void addGruppe(Long id, Gruppe gruppe) throws NullPointerException {
    Zeitslot zeitslot = findZeitslotByID(id);
    zeitslot.setGruppe(gruppe);
    zeitslotRepository.save(zeitslot);
  }

  public void createZeitslot(String wochentag, String uhrzeit) {
    Zeitslot zeitslot = new Zeitslot(wochentag, uhrzeit);
    zeitslotRepository.save(zeitslot);
  }

  public Zeitslot findZeitslotByID(Long id) {
    Optional<Zeitslot> zeitslot = zeitslotRepository.findById(id);
    if (zeitslot.isPresent()) {
      return zeitslot.get();
    } else {
      throw new NullPointerException("keine Zeislots für diesen ID vorhanden");
    }
//        return zeitslotRepository.findById(id).orElseThrow(() ->
//                 new ResponseStatusException(NOT_FOUND, "Kein Zeitslot mit id " + id + " vorhanden."));
  }

  public void deleteZeitslot(Long id) {
    zeitslotRepository.deleteById(id);
  }

}
