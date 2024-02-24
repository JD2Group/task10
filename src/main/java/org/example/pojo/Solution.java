package org.example.pojo;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "readyForReview")
@Builder
@ToString
@Entity
@Table(name = "solutions")
public class Solution implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column
    @Type(type = "text")
    private String response;

    @Column
    @Type(type = "text")
    private String review;

    @Column
    @Builder.Default
    private Boolean readyForReview = Boolean.FALSE;

    @Column
    private Integer mark;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public void setStudent(Student student) {
        this.student = student;
        if (student != null) {
            student.getSolutions().add(this);
        }
    }

    public void setTask(Task task) {
        this.task = task;
        if (task != null) {
            task.getSolutions().add(this);
        }
    }

}
