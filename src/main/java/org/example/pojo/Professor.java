package org.example.pojo;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "courses")
@ToString(exclude = "courses")

@SuperBuilder
@Entity
@Table(name = "professors")
public class Professor extends Person implements Serializable {

    @Builder.Default
    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();


   /* @Builder(builderMethodName = "professorBuilder")
    public Professor(String personName, String personSurname, Set<Course> courses) {
        super(null, personName, personSurname);
        this.courses = courses;
    }*/
}
