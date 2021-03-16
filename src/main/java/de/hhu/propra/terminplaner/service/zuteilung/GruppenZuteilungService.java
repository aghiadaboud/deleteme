package de.hhu.propra.terminplaner.service.zuteilung;

import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.service.gruppe.GruppeService;
import de.hhu.propra.terminplaner.service.student.StudentService;
import de.hhu.propra.terminplaner.service.tutor.TutorService;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GruppenZuteilungService {


  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private GruppeService gruppeService;

  private TutorService tutorService;
  private StudentService studentService;

  public GruppenZuteilungService(UebungService uebungService, ZeitslotService zeitslotService,
                                 TutorService tutorService, GruppeService gruppeService,
                                 StudentService studentService) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.tutorService = tutorService;
    this.gruppeService = gruppeService;
    this.studentService = studentService;
  }


  public void verteileGruppen(Long uebungid) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    Optional<List<Zeitslot>> allReservierteZeitslots =
        uebungService.getAllReservierteZeitslots(uebung);
    if (allReservierteZeitslots.isPresent()) {
      List<Zeitslot> zeitslots = allReservierteZeitslots.get();
      for (Zeitslot zeitslot : zeitslots) {
        List<Tutor> allTutorOfZeitslot = zeitslotService.getAllTutorOfZeitslot(zeitslot);
        List<Gruppe> allGruppenOfZeitslot = zeitslotService.getAllGruppenOfZeitslot(zeitslot);
        //List<Tutor> verteilteTutoren = new ArrayList<>();
        for (Gruppe gruppe : allGruppenOfZeitslot) {
//          List<Tutor> freieTutoren =
//              allTutorOfZeitslot.stream().filter(t -> !verteilteTutoren.contains(t)).collect(
//                  Collectors.toList());
//          gruppe.setTutor(freieTutoren.);
          Collections.shuffle(allTutorOfZeitslot);
          Tutor tutor = allTutorOfZeitslot.get(0);
          gruppe.setTutor(tutor);
          gruppeService.saveGruppe(gruppe);
          allTutorOfZeitslot.remove(tutor);
        }
      }
    }
  }

}
