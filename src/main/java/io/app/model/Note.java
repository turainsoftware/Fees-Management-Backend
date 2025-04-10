package io.app.model;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 100)
    private String title;
    @Column(length = 800)
    private String description;
    @ManyToOne
    @JoinColumn(name = "batchId",referencedColumnName = "id")
    private Batch batch;
    @ManyToOne
    @JoinColumn(name = "subjectId",referencedColumnName = "id")
    private Subject subject;
    private String fileName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    public void preCreate(){
        createdAt=new Date();
        updatedAt=new Date();
    }

    @PreUpdate
    public void updateCreate(){
        updatedAt=new Date();
    }

}
