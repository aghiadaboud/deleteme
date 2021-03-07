package de.hhu.terminplaner.model;


import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zeitslot {

  @Id
  private Long id;
  @NonNull
  private Integer kapazitaet = 0;
  private Termin termin;
  private Set<Tutor> tutoren = new HashSet<>();

  public Zeitslot(Termin termin, Tutor tutor) {
    //tutoren.forEach(this::addTutor);
    addTutor(tutor);
    this.termin = termin;
  }

  public int size() {
    return tutoren.size();
  }

  public boolean addTutor(Tutor tutor) {
    if (this.tutoren.add(tutor)) {
      kapazitaet = kapazitaet + 1;
    }
    return false;
  }
}
