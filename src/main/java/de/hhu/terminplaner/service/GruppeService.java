package de.hhu.terminplaner.service;

import de.hhu.terminplaner.model.gruppe.Gruppe;
import de.hhu.terminplaner.repos.GruppeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GruppeService {

  private GruppeRepository gruppeRepository;


  public GruppeService(GruppeRepository gruppeRepository) {
    this.gruppeRepository = gruppeRepository;
  }

  public List<Gruppe> findAll() {
    return gruppeRepository.findAll();
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
}
