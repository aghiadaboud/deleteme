package de.hhu.terminplaner.service.tutor;

import de.hhu.terminplaner.domain.tutor.Tutor;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.service.zeitslot.ZeitslotService;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

  ZeitslotService zeitslotService;

  public TutorService(ZeitslotService zeitslotService) {
    this.zeitslotService = zeitslotService;
  }

  public void addTutorZuZeitslot(Zeitslot zeitslot, Tutor tutor) {
    //tutor valid??
    if (zeitslot.addTutor(tutor)) {
      zeitslotService.saveZeitslot(zeitslot);
    }
  }

}
