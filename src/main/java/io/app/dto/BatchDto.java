package io.app.dto;

import io.app.model.Board;
import io.app.model.Class;
import io.app.model.Language;
import io.app.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BatchDto {
    private String name;
    private String batchSession;
    private Time startTime;
    private Time endTime;
    private Double monthlyFees;
    private Double monthlyExamFees;
    private Board board;
    private Language language;
    private Set<Subject> subjects;
    private Set<Class> classes;
}
