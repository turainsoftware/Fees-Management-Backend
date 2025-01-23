package io.app.services;

import io.app.dto.ApiResponse;
import io.app.model.Class;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface ClassServices {
    public ApiResponse createClass(String className);
    public ApiResponse createClass(Set<Class> classes);
    public List<Class> getAllClass();
    public ApiResponse deleteClassById(Long id);
    public ApiResponse deleteClassByName(String name);
    public ApiResponse deleteAll();
    public List<Class> autoSuggest(String query);
}
