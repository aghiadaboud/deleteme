package de.hhu.terminplaner.service.student;

import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.student.Student;
import de.hhu.terminplaner.service.gruppe.GruppeService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

  private GruppeService gruppeService;

  public StudentService(GruppeService gruppeService) {
    this.gruppeService = gruppeService;
  }

  public Map<Boolean, String> addStudentZuGruppe(Gruppe gruppe, Student student) {
    Map<Boolean, String> nachricht = new HashMap<>();
//    student valid??
//        if (!validStudent(student)) {
//      return nachricht.put(false, "Studentinfos sind nicht gültig");
//    }
    if (gruppe.size() < 5) {
      gruppe.addStudent(student);
      gruppeService.saveGruppe(gruppe);
      nachricht.put(true, "Student wurde erfolgreich hinzugefügt");
      return nachricht;
    }
    nachricht.put(false, "Diese Gruppe ist voll");
    return nachricht;
  }
}
