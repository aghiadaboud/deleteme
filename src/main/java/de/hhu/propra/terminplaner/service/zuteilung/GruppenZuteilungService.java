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
import java.util.List;
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

    List<Zeitslot> allZeitslotOfUebung =
        uebungService.getAllZeitslotOfUebung(uebung);

    if (!allZeitslotOfUebung.isEmpty()) {
      for (Zeitslot zeitslot : allZeitslotOfUebung) {
        List<Tutor> allTutorOfZeitslot = zeitslotService.getAllTutorOfZeitslot(zeitslot);
        List<Gruppe> allGruppenOfZeitslot = zeitslotService.getAllGruppenOfZeitslot(zeitslot);
        for (Gruppe gruppe : allGruppenOfZeitslot) {
          //Collections.shuffle(allTutorOfZeitslot);
          Tutor tutor = allTutorOfZeitslot.get(0);
          gruppe.setTutor(tutor);
          //gruppeService.saveGruppe(gruppe);
          //allTutorOfZeitslot.remove(tutor);
        }
      }
    }
  }

}
