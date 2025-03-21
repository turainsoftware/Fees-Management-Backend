package io.app.services;

import io.app.dto.AnalysisResponse;
import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.Projections.TeacherProjection;
import io.app.dto.TeacherDto;
import io.app.model.*;
import io.app.model.Class;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface TeacherService {
    public TeacherDto profile(String authToken);
    public TeacherProjection profileLess(String authToken);
    public ApiResponse updateClasses(Set<Class> classes, String token);
    public ApiResponse updateBoards(Set<Board> boards, String token);
    public ApiResponse updateSubjects(Set<Subject> subjects, String token);
    public ApiResponse updateLanguage(Set<Language> languages, String token);
    public boolean teacherExistByMobileNumber(String mobileNumber);
    public Set<BatchDto> allBatch(String authToken);
    public Set<Subject> subjectsOfTeachers(String authToken);

    AnalysisResponse getStudentAnalysis(String authToken);

    AnalysisResponse getSubjectAnalysis(String authToken);

    // Update Profile
    public ApiResponse updateProfile(String authToken,String name,String email,Gender gender);
    public ApiResponse updateProfilePicture(String authToken, MultipartFile profileImage) throws IOException;

    public Set<Language> languages(String authToken);
    public Set<Board> boards(String authToken);
    public Set<Class> classes(String authToken);
}
