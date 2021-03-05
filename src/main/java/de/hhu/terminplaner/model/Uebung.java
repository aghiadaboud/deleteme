package de.hhu.terminplaner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Uebung {


    @Id
    private Long id;

    private Set<Zeitslot> zeitslots = new HashSet<>();
    private String name;
    private Boolean gruppenanmeldung;
    private Zeitraum zeitraum;

    public Uebung(String name, Boolean gruppenanmeldung, Zeitraum zeitraum) {
        this.name = name;
        this.gruppenanmeldung = gruppenanmeldung;
        this.zeitraum = zeitraum;
    }

    public Uebung(Set<Zeitslot> zeitslots, String name, Boolean gruppenanmeldung, Zeitraum zeitraum) {
        this.zeitslots = zeitslots;
        this.name = name;
        this.gruppenanmeldung = gruppenanmeldung;
        this.zeitraum = zeitraum;
    }


    public int size() {
        return zeitslots.size();
    }

}
