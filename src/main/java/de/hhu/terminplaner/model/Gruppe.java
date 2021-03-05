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
public class Gruppe {

    @Id
    private Long id;
    private Set<Student> studenden = new HashSet<>();
    private String name;
//  private Integer min;
//  private Integer max;


    public int size() {
        return studenden.size();
    }
}
