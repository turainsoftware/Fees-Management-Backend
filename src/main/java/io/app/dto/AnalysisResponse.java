package io.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnalysisResponse {
    private double current;
    private double previous;
    private double percentage;
    private String trend;
}
