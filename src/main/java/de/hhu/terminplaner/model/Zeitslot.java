package de.hhu.terminplaner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zeitslot {


    @Id
    private Long id;
    //private LocalDate datum;
    private String wochentag;
    private String uhr;
    private Gruppe gruppe;

    public Zeitslot(String wochentag, String uhr) {
        this.wochentag = wochentag;
        this.uhr = uhr;
    }
}
