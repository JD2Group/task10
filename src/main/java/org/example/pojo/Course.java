package org.example.pojo;

import lombok.*;
import org.example.dao.CourseDao;
import org.hibernate.PropertyValueException;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"tasks", "students"})
@ToString(exclude = {"tasks", "students"})
@Builder
@Entity
@Table(name = "courses")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Builder.Default
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Task> tasks = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    public void setProfessor(Professor professor) throws PropertyValueException {

        if (CourseDao.DELETED_COURSE_ID != this.id) {
            this.professor = professor;
            if (professor != null) {
                professor.getCourses().add(this);
            }
        } else {
            throw new PropertyValueException("You cannot set Id of any professor in this row!", "Course", "professor_id");
        }
    }
}
