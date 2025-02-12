package io.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeesAnalysisResponse {
    private double currentMonthFees;
    private double previousMonthFees;
    private double percentageChange;
    private String trend;
}
