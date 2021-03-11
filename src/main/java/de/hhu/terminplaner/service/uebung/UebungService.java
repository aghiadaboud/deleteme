package de.hhu.terminplaner.service.uebung;

import de.hhu.terminplaner.domain.uebung.Uebung;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.repos.UebungRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UebungService {

  private UebungRepository uebungRepository;


  public UebungService(UebungRepository uebungRepository) {
    this.uebungRepository = uebungRepository;
  }

  public List<Uebung> findAllUebungen() {
    return uebungRepository.findAll();
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

  public Long createUebung(String name, Boolean gruppenanmeldung, LocalDate von, LocalDate bis) {
    Uebung uebung = new Uebung(name, gruppenanmeldung, von, bis);
    uebungRepository.save(uebung);
    return uebung.getId();
  }


  public List<Zeitslot> getAllZeitslotOfUebung(Uebung uebung) {
    List<Zeitslot> zeitslots = new ArrayList<>();
    uebung.getZeitslots().forEach(zeitslot -> zeitslots.add(zeitslot));
    return zeitslots;
  }

  public Optional<List<Zeitslot>> getAllFreieZeitslotOfUebung(Uebung uebung) {
    List<Zeitslot> zeitslots = getAllZeitslotOfUebung(uebung);
    List<Zeitslot> freieZeitslots =
        zeitslots.stream()
            .filter(zeitslot -> !zeitslot.getReserviert())
            .collect(Collectors.toList());
    if (freieZeitslots.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(freieZeitslots);
  }


  public Optional<List<Zeitslot>> getAllReservierteZeitslots(Uebung uebung) {
    List<Zeitslot> zeitslots = getAllZeitslotOfUebung(uebung);
    List<Zeitslot> reservierteZeitslots =
        zeitslots.stream()
            .filter(zeitslot -> zeitslot.getReserviert())
            .collect(Collectors.toList());
    if (reservierteZeitslots.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(reservierteZeitslots);
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

  public Uebung saveUebung(Uebung uebung) {
    uebungRepository.save(uebung);
    return uebung;
  }
}
