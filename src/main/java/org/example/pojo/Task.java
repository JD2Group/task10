package org.example.pojo;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "solutions")
@Builder
@ToString(exclude = "solutions")
@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column
    @Type(type = "text")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder.Default
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private Set<Solution> solutions = new HashSet<>();
}
