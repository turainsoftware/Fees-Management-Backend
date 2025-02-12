package io.app.services;

import io.app.dto.AnalysisResponse;
import io.app.dto.TeacherFeesHistoryDto;

import java.util.List;

public interface FeesHistoryService {
    public List<TeacherFeesHistoryDto> feesByTeacher(String authToken);
    public List<TeacherFeesHistoryDto> latestFeesByTeacher(String authToken);
    public List<TeacherFeesHistoryDto> feesByTeacherInRange(String authToken,int pageNo,int size);
    public AnalysisResponse feesAnalysisByTeacherAndMonths(String authToken);
}
