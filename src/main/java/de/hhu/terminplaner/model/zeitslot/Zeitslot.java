package de.hhu.terminplaner.model.zeitslot;


import de.hhu.terminplaner.model.termin.Termin;
import de.hhu.terminplaner.stereotype.AggregateRoot;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AggregateRoot
public class Zeitslot {

  @Id
  private Long id;
  @NonNull
  private Integer kapazitaet = 0;
  private Termin termin;
  private Set<Tutor> tutoren = new HashSet<>();

  public Zeitslot(Termin termin) {
    //tutoren.forEach(this::addTutor);
    this.termin = termin;
  }

  public int size() {
    return tutoren.size();
  }

//  public boolean addTutor(Tutor tutor) {
//    if (this.tutoren.add(tutor)) {
//      kapazitaet = kapazitaet + 1;
//    }
//    return false;
//  }

  public void increaseKapazitaet(Integer number) {
    this.kapazitaet += number;
  }

  public boolean addTutor(Tutor tutor) {
    return this.tutoren.add(tutor);
  }
}
