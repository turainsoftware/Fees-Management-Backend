package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.excetptions.DuplicateFoundException;
import io.app.model.Subject;
import io.app.repository.SubjectRepository;
import io.app.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository repository;

    @Override
    public ApiResponse createSubject(String subjectName) {
        boolean isExist=repository.existsByName(subjectName);
        if (isExist){
            throw new DuplicateFoundException("Subject already exists");
        }
        Subject subject=Subject.builder()
                .name(subjectName)
                .build();
        repository.save(subject);
        return ApiResponse.builder()
                .message("Subject Created Successfully")
                .status(true)
                .build();
    }

    @Override
    public ApiResponse createSubject(Set<Subject> subjects) {
        repository.saveAll(subjects);
        return ApiResponse.builder()
                .message("Subjects added Successfully")
                .status(true)
                .build();
    }

    @Override
    public List<Subject> getAllSubjects() {
        return repository.findAll();
    }

    @Override
    public ApiResponse deleteSubjectsById(Long id) {
        repository.deleteById(id);
        return ApiResponse.builder()
                .message("Subject Deleted Successfully")
                .status(true)
                .build();
    }

    @Override
    public ApiResponse deleteSubjectsByName(String name) {
        Subject subject=repository.findByName(name)
                .orElseThrow(()->new ResourceAccessException("No such Subjects present"));
        repository.delete(subject);
        return ApiResponse.builder()
                .message("Subject Deleted Successfully")
                .status(true)
                .build();
    }

    @Override
    public ApiResponse deleteAll() {
        repository.deleteAll();
        return ApiResponse.builder()
                .status(true)
                .message("All Subjects deleted Successfully")
                .build();
    }

    @Override
    public List<Subject> autoSuggest(String query) {
        return repository.findByNameContaining(query);
    }
}
