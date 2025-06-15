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
    public BatchDto getBatchById(long id);
    public ApiResponse updateWholeBatch(BatchDto batchDto);
    public ApiResponse updateBatchFees(String authToken,long batchId,
                                       double monthlyFees,double monthlyExamFees);
    public ApiResponse updateBatchSubjects(long batchId,Set<Long> subjectsId);
    public ApiResponse updateBatchSchedule(String authToken,
                                           long batchId,int startYear,
                                           int startMonth,int endYear,
                                           int endMonth,Set<Days> days,
                                           LocalTime startTime,LocalTime endTime);
    public ApiResponse updateBatchDetails(long batchId,String name,long languageId,long boardId,Set<Long> classesId);
    public ApiResponse removeStudentFromBatch(Long batchId,Long studentId);
    public ApiResponse deactivateBatch(Long batchId);
}
