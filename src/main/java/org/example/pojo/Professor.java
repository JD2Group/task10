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
@ToString(callSuper = true,exclude = "courses")

@SuperBuilder
@Entity
@Table(name = "professors")
public class Professor extends Person implements Serializable {

    @Builder.Default
    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();
}
