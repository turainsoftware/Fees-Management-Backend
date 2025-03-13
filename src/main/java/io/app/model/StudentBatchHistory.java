package io.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentBatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "batchId",nullable = false)
    private Batch batch;
    @ManyToOne
    @JoinColumn(name = "studentId",nullable = false)
    private Student student;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;

    @PrePersist
    public void prePersist(){
        if(startDate==null){
            startDate=new Date();
        }
    }

}
