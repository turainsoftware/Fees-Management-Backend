package io.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeesDto {
    private long id;
    private Long studentId;
    private Long batchId;
    private int startYear;
    private int startMonth;
    private int endYear;
    private int endMonth;
    private double totalFees;
    private double totalPaid;
    private double totalDue;
    private double monthlyFees;
    private List<FeesHistoryDto> feesHistories;
}
