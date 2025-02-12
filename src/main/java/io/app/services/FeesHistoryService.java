package io.app.services;

import io.app.dto.FeesAnalysisResponse;
import io.app.dto.TeacherFeesHistoryDto;
import io.app.model.Teacher;

import java.util.List;

public interface FeesHistoryService {
    public List<TeacherFeesHistoryDto> feesByTeacher(String authToken);
    public List<TeacherFeesHistoryDto> latestFeesByTeacher(String authToken);
    public List<TeacherFeesHistoryDto> feesByTeacherInRange(String authToken,int pageNo,int size);
    public FeesAnalysisResponse feesAnalysisByTeacherAndMonths(String authToken);
}
