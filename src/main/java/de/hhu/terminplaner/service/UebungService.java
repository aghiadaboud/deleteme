package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.uebung.Anmeldungfrist;
import de.hhu.terminplaner.model.uebung.Uebung;
import de.hhu.terminplaner.model.zeitslot.Zeitslot;
import de.hhu.terminplaner.repos.UebungRepository;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UebungService {

  private UebungRepository uebungRepository;


  public UebungService(UebungRepository uebungRepository) {
    this.uebungRepository = uebungRepository;
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

  public Long createUebung(String name, Boolean gruppenanmeldung, Anmeldungfrist anmeldungfrist) {
    Uebung uebung = new Uebung(name, gruppenanmeldung, anmeldungfrist);
    uebungRepository.save(uebung);
    return uebung.getId();
  }


  public void addZeitslot(Long uebungid, @NonNull Zeitslot zeitslot) throws NullPointerException {
    Uebung uebung = findUebungById(uebungid);
    uebung.getZeitslots().add(zeitslot);
    uebungRepository.save(uebung);
  }


  public void updateName(Long uebungid, @NonNull String newName) {
    Uebung uebung = findUebungById(uebungid);
    uebung.setName(newName);
    uebungRepository.save(uebung);
  }

  public void deleteZeitslot(Long uebungid, @NonNull Zeitslot zeitslot)
      throws NullPointerException {
    Uebung uebung = findUebungById(uebungid);
    uebung.getZeitslots().remove(zeitslot);
    uebungRepository.save(uebung);
  }
}
