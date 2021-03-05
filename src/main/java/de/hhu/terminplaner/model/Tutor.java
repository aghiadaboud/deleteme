package de.hhu.terminplaner.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tutor {

    @Id
    private Long id;

    private String githubname;
    private Zeitslot zeitslot;


}
