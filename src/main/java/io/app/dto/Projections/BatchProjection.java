package io.app.dto.Projections;

import io.app.model.Days;

import java.time.LocalTime;
import java.util.Set;

public record BatchProjection(
        Long id,
        String name,
        int startYear,
        int endYear,
        int startMonth,
        int endMonth,
        LocalTime startTime,
        LocalTime endTime,
        Set<Days> days,
        Double monthlyFees,
                              Double monthlyExamFees) {
}
