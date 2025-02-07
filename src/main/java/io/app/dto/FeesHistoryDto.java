package io.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeesHistoryDto {
    private long id;
    private double amountPaid;
    private int year;
    private int month;
    private Date paymentDate;
    private String description;
}
