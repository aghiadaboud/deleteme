package de.hhu.propra.terminplaner.service.zuteilung;

import de.hhu.propra.terminplaner.domain.gruppe.Gruppe;
import de.hhu.propra.terminplaner.domain.student.Student;
import de.hhu.propra.terminplaner.domain.tutor.Tutor;
import de.hhu.propra.terminplaner.domain.uebung.Uebung;
import de.hhu.propra.terminplaner.domain.zeitslot.Zeitslot;
import de.hhu.propra.terminplaner.repos.TutorRepository;
import de.hhu.propra.terminplaner.service.gruppe.GruppeService;
import de.hhu.propra.terminplaner.service.student.StudentService;
import de.hhu.propra.terminplaner.service.tutor.TutorService;
import de.hhu.propra.terminplaner.service.uebung.UebungService;
import de.hhu.propra.terminplaner.service.zeitslot.ZeitslotService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GruppenZuteilungService {


  private UebungService uebungService;
  private ZeitslotService zeitslotService;

  private GruppeService gruppeService;

  private TutorService tutorService;
  private StudentService studentService;
  private TutorRepository tutorRepository;

  public GruppenZuteilungService(UebungService uebungService, ZeitslotService zeitslotService,
                                 TutorService tutorService, GruppeService gruppeService,
                                 StudentService studentService, TutorRepository tutorRepository) {
    this.uebungService = uebungService;
    this.zeitslotService = zeitslotService;
    this.tutorService = tutorService;
    this.gruppeService = gruppeService;
    this.studentService = studentService;
    this.tutorRepository = tutorRepository;
  }


  public void verteileTutorenAufGruppenGruppenanmeldung(Long uebungid) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    //Optional<Uebung> letzteUebung = uebungService.ladeVorlage();
    List<Zeitslot> allZeitslotOfUebung =
        uebungService.getAllZeitslotOfUebung(uebung);
    verteileTutorenAufGruppen(allZeitslotOfUebung);
  }

  public void passeGruppenAnAndVerteielIndividualanmeldung(Long uebungid) {
    Uebung uebung = uebungService.findUebungById(uebungid);
    //Optional<Uebung> letzteUebung = uebungService.ladeVorlage();
    List<Zeitslot> allZeitslotOfUebung =
        uebungService.getAllZeitslotWithMoreThanOneTutor(uebung);
    passeGruppenAnIndividualanmeldung(allZeitslotOfUebung);
    zeitslotService.berechneNeueKapatzitaetAndZustandNachZuteilung(
        uebungService.getAllZeitslotOfUebung(uebung));
    verteileTutorenAufGruppen(uebungService.getAllZeitslotOfUebung(uebung));
  }

  private void passeGruppenAnIndividualanmeldung(List<Zeitslot> allZeitslotOfUebung) {
    if (!allZeitslotOfUebung.isEmpty()) {
      for (Zeitslot zeitslot : allZeitslotOfUebung) {
        List<Tutor> allTutorOfZeitslot = zeitslotService.getAllTutorOfZeitslot(zeitslot);
        Gruppe gruppe = zeitslotService.getAllGruppenOfZeitslot(zeitslot).get(0);
        int anzahlGruppeMitglieder = gruppe.size();
        int berechneBenoetigteGruppenanzahl =
            berechneBenoetigteGruppenanzahl(anzahlGruppeMitglieder);
        List<Gruppe> neueGruppen = erstelleGruppenList(gruppe, berechneBenoetigteGruppenanzahl);
        List<Gruppe> befuellteGruppen = verteileStudentenAufGruppenIndividualanmeldung(
            gruppeService.getAllGruppenmitglieder(gruppe), neueGruppen);
        befuellteGruppen.forEach(zeitslot::addGruppe);
        zeitslot.getGruppen().remove(gruppe);
        zeitslotService.saveZeitslot(zeitslot);
      }
    }
  }


  private void verteileTutorenAufGruppen(List<Zeitslot> zeitslots) {
    if (!zeitslots.isEmpty()) {
      for (Zeitslot zeitslot : zeitslots) {
        List<Tutor> allTutorOfZeitslot = zeitslotService.getAllTutorOfZeitslot(zeitslot);
        List<Gruppe> allGruppenOfZeitslot = zeitslotService.getAllGruppenOfZeitslot(zeitslot);
        for (Gruppe gruppe : allGruppenOfZeitslot) {
          Collections.shuffle(allTutorOfZeitslot);
          Tutor tutor = allTutorOfZeitslot.get(0);
          tutor.setGruppeid(gruppe.getId());
          tutorRepository.save(tutor);
          allTutorOfZeitslot.remove(tutor);
        }
      }
    }
  }


  private int berechneBenoetigteGruppenanzahl(int anzahlStudenten) {
    if (anzahlStudenten == 0) {
      return 0;
    }
    if (anzahlStudenten <= 5) {
      return 1;
    }
    return 1 + berechneBenoetigteGruppenanzahl(anzahlStudenten - 5);
  }

  private List<Gruppe> erstelleGruppenList(Gruppe gruppe, int anzahl) {
    List<Gruppe> gruppen = new ArrayList<>();
    for (int i = 0; i < anzahl; i++) {
      gruppen.add(new Gruppe("" + i + "-" + gruppe.getName()));
    }
    return gruppen;
  }

  private List<Gruppe> verteileStudentenAufGruppenIndividualanmeldung(List<Student> studenten,
                                                                      List<Gruppe> gruppen) {
    int index = 0;
    while (!studenten.isEmpty()) {
      index = index % gruppen.size();
      Collections.shuffle(studenten);
      gruppen.get(index).addStudent(studenten.get(0));
      studenten.remove(studenten.get(0));
      index = index + 1;
    }
    return gruppen;
  }

}
