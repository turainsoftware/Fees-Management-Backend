package io.app.dto;

import io.app.model.*;
import io.app.model.Class;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BatchDto {
    private String name;
    private String batchSession;
    private LocalTime startTime;
    private LocalTime endTime;
    private Set<Days> days=new HashSet<>();
    private Double monthlyFees;
    private Double monthlyExamFees;
    private Board board;
    private Language language;
    private Set<Subject> subjects;
    private Set<Class> classes;
    private Date createdAt;
    private Date updatedAt;
}
