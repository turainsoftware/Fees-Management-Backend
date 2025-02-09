package io.app.services;

import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.TeacherDto;
import io.app.model.*;
import io.app.model.Class;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface TeacherService {
    public TeacherDto profile(String authToken);
    public ApiResponse updateClasses(Set<Class> classes, String token);
    public ApiResponse updateBoards(Set<Board> boards, String token);
    public ApiResponse updateSubjects(Set<Subject> subjects, String token);
    public ApiResponse updateLanguage(Set<Language> languages, String token);

    public Set<BatchDto> allBatch(String authToken);
}
