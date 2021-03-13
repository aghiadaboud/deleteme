package de.hhu.propra.terminplaner.service.student;

import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.student.Student;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.service.gruppe.GruppeService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

  private GruppeService gruppeService;
  private ZeitslotService zeitslotService;

  public StudentService(GruppeService gruppeService, ZeitslotService zeitslotService) {
    this.gruppeService = gruppeService;
    this.zeitslotService = zeitslotService;
  }

  public Map<Boolean, String> addStudentZuGruppe(Zeitslot zeitslot, Gruppe gruppe, Student student,
                                                 boolean gruppenAnmeldung) {
    int anzahlErlaubteMitgliederIndividualanmeldung =
        zeitslotService.studentenKapazitaetofZeitslotForIndividualanmeldung(zeitslot);
    if (gruppenAnmeldung) {
      return addStudentZuGruppeAndUpdateZeitslot(zeitslot, gruppe, student, 5, 0);
    } else {
      return addStudentZuGruppeAndUpdateZeitslot(zeitslot, gruppe, student,
          anzahlErlaubteMitgliederIndividualanmeldung, 1);
    }
  }


  private Map<Boolean, String> addStudentZuGruppeAndUpdateZeitslot(Zeitslot zeitslot, Gruppe gruppe,
                                                                   Student student,
                                                                   int erlaubteGruppengroesse,
                                                                   int number) {
    Map<Boolean, String> nachricht = new HashMap<>();
//    student valid??
//        if (!validStudent(student)) {
//      return nachricht.put(false, "Studentinfos sind nicht gültig");
//    }
    if (gruppe.size() < erlaubteGruppengroesse) {
      gruppe.addStudent(student);
      zeitslot.decreaseKapazitaet(number);
      if (zeitslot.getKapazitaet() == 0) {
        zeitslot.setReserviert(true);
      }
      zeitslotService.saveZeitslot(zeitslot);
      gruppeService.saveGruppe(gruppe);
      nachricht.put(true, "Student wurde erfolgreich hinzugefügt");
      return nachricht;
    }
    nachricht.put(false, "Diese Gruppe ist voll");
    return nachricht;
  }
}
