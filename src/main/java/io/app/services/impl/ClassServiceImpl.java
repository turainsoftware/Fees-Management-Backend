package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.excetptions.DuplicateFoundException;
import io.app.model.Class;
import io.app.repository.ClassRepository;
import io.app.services.ClassServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassServices {

    private final ClassRepository repository;

    @Override
    public ApiResponse createClass(String className) {
        boolean isExists=repository.existsByName(className);
        if(isExists){
            throw new DuplicateFoundException("Class Already Exists");
        }
        Class dataClass=Class.builder()
                .name(className)
                .build();
        repository.save(dataClass);
        return ApiResponse.builder()
                .status(true)
                .message("Class Created Successfully")
                .build();
    }

    @Override
    public ApiResponse createClass(Set<Class> classes) {
        repository.saveAll(classes);
        return ApiResponse.builder()
                .message("Classes Added Successfully")
                .status(true)
                .build();
    }

    @Override
    public List<Class> getAllClass() {
        return repository.findAll();
    }

    @Override
    public ApiResponse deleteClassById(Long id) {
        repository.deleteById(id);
        return ApiResponse.builder()
                .message("Deleted Successfully")
                .status(true)
                .build();
    }

    @Override
    public ApiResponse deleteClassByName(String name) {
        Class classData=repository.findByName(name);
        repository.delete(classData);
        return ApiResponse.builder()
                .status(true)
                .message("Class Deleted Successfully")
                .build();
    }

    @Override
    public ApiResponse deleteAll() {
        repository.deleteAll();
        return ApiResponse.builder()
                .status(true)
                .message("All Classes Deleted Successfully")
                .build();
    }

    @Override
    public List<Class> autoSuggest(String query) {
        return repository.findByNameContaining(query);
    }
}
