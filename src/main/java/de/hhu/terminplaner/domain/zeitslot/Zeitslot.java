package de.hhu.terminplaner.domain.zeitslot;


import de.hhu.terminplaner.domain.gruppe.Gruppe;
import de.hhu.terminplaner.domain.tutor.Tutor;
import de.hhu.terminplaner.stereotype.AggregateRoot;
import java.time.LocalDate;
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
  @NonNull
  private LocalDate datum;
  @NonNull
  private String uhrzeit;
  @NonNull
  private Boolean reserviert = false;
  private Set<Gruppe> gruppen = new HashSet<>();
  private Set<Tutor> tutoren = new HashSet<>();

  public Zeitslot(@NonNull LocalDate datum, String uhrzeit) {
    //tutoren.forEach(this::addTutor);
    this.datum = datum;
    this.uhrzeit = uhrzeit;
  }

  public int tutorenAnzahl() {
    return tutoren.size();
  }

  public int gruppenAnzahl() {
    return gruppen.size();
  }

  public boolean addGruppe(Gruppe gruppe) {
    return this.gruppen.add(gruppe);
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
