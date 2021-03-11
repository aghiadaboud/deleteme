package de.hhu.terminplaner.service.student;

import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.student.Student;
import de.hhu.terminplaner.service.gruppe.GruppeService;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

  private GruppeService gruppeService;

  public StudentService(GruppeService gruppeService) {
    this.gruppeService = gruppeService;
  }

  public void addStudentZuGruppe(Gruppe gruppe, Student student) {
    //student valid??
    if (gruppe.addStudent(student)) {
      gruppeService.saveGruppe(gruppe);
    }
  }
}
