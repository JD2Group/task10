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
@EqualsAndHashCode(callSuper = true, exclude = "solutions")
@ToString(callSuper = true, exclude = {"solutions", "courses"})

@SuperBuilder
@Entity
@Table(name = "students")
public class Student extends Person implements Serializable {

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "students_courses",
        joinColumns = {@JoinColumn(name = "student_id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id")})
    private Set<Course> courses = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private Set<Solution> solutions = new HashSet<>();

    public void addCourse(Course course) {
        this.courses.add(course);
        if (course != null) {
            course.getStudents().add(this);
        }
    }
}

