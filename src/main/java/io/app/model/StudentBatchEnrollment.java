package io.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class StudentBatchEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int year;
    @Column(nullable = false)
    private int month;
    @ManyToOne
    @JoinColumn(name = "batchId",nullable = false)
    private Batch batch;
    @ManyToOne
    @JoinColumn(name = "studentId",nullable = false)
    private Student student;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    public void preCreate(){
        this.createdAt=new Date();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt=new Date();
    }

}
