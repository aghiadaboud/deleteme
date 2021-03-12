package de.hhu.terminplaner.service.tutor;

import de.hhu.terminplaner.domain.tutor.Tutor;
import de.hhu.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.terminplaner.service.zeitslot.ZeitslotService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

  ZeitslotService zeitslotService;

  public TutorService(ZeitslotService zeitslotService) {
    this.zeitslotService = zeitslotService;
  }

  public Map<Boolean, String> addTutorZuZeitslot(Zeitslot zeitslot, Tutor tutor) {
    Map<Boolean, String> nachricht = new HashMap<>();
//    student valid??
//        if (!validTutor(tutor)) {
//      return nachricht.put(false, "Tutorinfos sind nicht gültig");
//    }
    if (zeitslot.addTutor(tutor)) {
      zeitslotService.saveZeitslot(zeitslot);
      nachricht.put(true, "Tutor wurde erfolgreich hinzugefügt");
      return nachricht;
    }
    nachricht.put(false, "Tutor konnte nicht hinzugefügt werden");
    return nachricht;
  }

}
