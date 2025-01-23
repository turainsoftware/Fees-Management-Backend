package io.app.services;

import io.app.dto.ApiResponse;
import io.app.model.Subject;

import java.util.List;
import java.util.Set;

public interface SubjectService {
    public ApiResponse createSubject(String subjectName);
    public ApiResponse createSubject(Set<Subject> subjects);
    public List<Subject> getAllSubjects();
    public ApiResponse deleteSubjectsById(Long id);
    public ApiResponse deleteSubjectsByName(String name);
    public ApiResponse deleteAll();
    public List<Subject> autoSuggest(String query);
}
