package io.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String batchSession;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;
    private Set<Days> days = new HashSet<>();
    @Column(nullable = false)
    private Double monthlyFees;
    private Double monthlyExamFees;

    @ManyToOne
    @JoinColumn(name = "boardId", referencedColumnName = "id", nullable = false)
    private Board board;
    @ManyToOne
    @JoinColumn(name = "languageId", referencedColumnName = "id", nullable = false)
    private Language language;
    @ManyToMany
    @JoinTable(
            name = "BatchSubjects",
            joinColumns = @JoinColumn(name = "batchId"),
            inverseJoinColumns = @JoinColumn(name = "subjectId")
    )
    @Column(nullable = false)
    private Set<Subject> subjects;

    @ManyToMany
    @JoinTable(
            name = "BatchClasses",
            joinColumns = @JoinColumn(name = "batchId"),
            inverseJoinColumns = @JoinColumn(name = "classId")
    )
    @Column(nullable = false)
    private Set<Class> classes;

    @ManyToOne
    @JoinColumn(name = "teacherId", referencedColumnName = "id", nullable = false)
    private Teacher teacher;


    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    @PrePersist
    public void preCreate() {
        createdAt = new Date();
    }

    @Temporal(TemporalType.TIMESTAMP)
    public void preUpdate() {
        updatedAt = new Date();
    }

}
