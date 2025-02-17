package io.app.services;

import io.app.dto.AnalysisResponse;
import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.Projections.BatchProjection;
import io.app.model.Batch;
import io.app.model.Days;
import io.app.model.Teacher;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface BatchService {
    public ApiResponse createBatch(String authToken, BatchDto batchDto);
    public List<BatchDto> getAllBatch(String authToken);

    public boolean hasTimeConflict(Teacher teacher, LocalTime startTime,LocalTime endTime, Set<Days> days);
    public String extractJwt(String authToken);

    public AnalysisResponse batchAnalysis(String authToken);
    public List<BatchProjection> getAllBatchDetailsWithSpecificDetails(String authToken);
}
