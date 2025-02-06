package io.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormatSymbols;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeesHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "feesId",nullable = false)
    private Fees fees;

    private double amountPaid;
    private int year;
    private int month;
    private Date paymentDate;
    private String description;

    @PrePersist
    public void updateDescription(){
        String monthName=new DateFormatSymbols().getMonths()[month-1];
        this.description=monthName+" "+year+" Fees";
    }
}
