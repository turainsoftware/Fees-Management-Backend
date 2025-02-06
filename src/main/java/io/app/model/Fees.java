package io.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Fees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "studentId",nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "batchId",nullable = false)
    private Batch batch;

    private int startYear;
    private int startMonth;
    private int endYear;
    private int endMonth;

    private double totalFees;
    private double totalPaid;
    private double totalDue;

    private double monthlyFees;


    @OneToMany(mappedBy = "fees",cascade = CascadeType.ALL)
    private List<FeesHistory> feesHistories=new ArrayList<>();


    public void createFees(){
        YearMonth start = YearMonth.of(startYear, startMonth);
        YearMonth end = YearMonth.of(endYear, endMonth);
        long totalMonths = ChronoUnit.MONTHS.between(start,end)+1;

        this.totalFees=totalMonths * this.monthlyFees;
        this.totalDue=this.totalFees;
    }

}
