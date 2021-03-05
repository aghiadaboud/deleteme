package de.hhu.terminplaner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zeitraum {

    private LocalDate von;
    private LocalDate bis;
}
