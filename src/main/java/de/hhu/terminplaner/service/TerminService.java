package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.Gruppe;
import de.hhu.terminplaner.model.Termin;
import de.hhu.terminplaner.repos.TerminRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TerminService {

  private TerminRepository terminRepository;

  public TerminService(TerminRepository terminRepository) {
    this.terminRepository = terminRepository;
  }

  public void addGruppe(Long id, Gruppe gruppe) throws NullPointerException {
    Termin termin = findTerminById(id);
    boolean added = termin.addGruppe(gruppe);
    terminRepository.save(termin);
  }

  private Termin findTerminById(Long id) {
    Optional<Termin> termin = terminRepository.findById(id);
    if (termin.isPresent()) {
      return termin.get();
    } else {
      throw new NullPointerException("kein Termin für diesen ID vorhanden");
    }
//        return zeitslotRepository.findById(id).orElseThrow(() ->
//                 new ResponseStatusException(NOT_FOUND, "Kein Termin mit id " + id + " vorhanden."));
  }
}
