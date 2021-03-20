package de.hhu.propra.terminplaner.service.tutor;

import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

  private ZeitslotService zeitslotService;

  public TutorService(ZeitslotService zeitslotService) {
    this.zeitslotService = zeitslotService;
  }

  public Map<Boolean, String> checkAnmeldungmodusAndaddTutorZuZeitslot(
      Zeitslot zeitslot, Tutor tutor, boolean gruppenAnmeldung) {
    if (gruppenAnmeldung) {
      return addTutorZuZeitslotAndIncreaseKapazitaet(zeitslot, tutor, 1);
    } else {
      return addTutorZuZeitslotAndIncreaseKapazitaet(zeitslot, tutor, 5);
    }
  }


  private Map<Boolean, String> addTutorZuZeitslotAndIncreaseKapazitaet(
      Zeitslot zeitslot, Tutor tutor, int anzahl) {
    Map<Boolean, String> nachricht = new HashMap<>();
    if (tutorAlreadyExists(zeitslot, tutor)) {
      nachricht.put(false, "Tutor wurde bereits hinzugefügt");
      return nachricht;
    }
    boolean addedTutor = zeitslot.addTutor(tutor);
    if (addedTutor) {
      zeitslot.increaseZeitslotKapazitaet(anzahl);
      zeitslotService.saveZeitslot(zeitslot);
      nachricht.put(true, "Tutor wurde erfolgreich hinzugefügt");
      return nachricht;
    }
    nachricht.put(false, "Tutor konnte nicht hinzugefügt werden");
    return nachricht;
  }

  private boolean tutorAlreadyExists(Zeitslot zeitslot, Tutor newtutor) {
    return zeitslot.getTutoren().stream()
        .anyMatch(tutor -> tutor.getGithubname().equals(newtutor.getGithubname()));
  }

}
