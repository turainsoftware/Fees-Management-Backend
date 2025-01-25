package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.TeacherDto;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.*;
import io.app.model.Class;
import io.app.repository.TeacherRepository;
import io.app.services.JwtService;
import io.app.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository repository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public TeacherDto profile(String authToken) {
        Teacher teacher=repository.findByPhone(getMobileByToken(authToken))
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        TeacherDto teacherDto=modelMapper.map(teacher,TeacherDto.class);
        return teacherDto;
    }

    @Override
    public ApiResponse updateClasses(Set<Class> classes, String token) {
        Teacher teacher=repository.findByPhone(getMobileByToken(token))
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        teacher.setClasses(classes);
        repository.save(teacher);
        return ApiResponse.builder()
                .message("Classes Updated successfully")
                .status(true)
                .build();
    }

    @Override
    public ApiResponse updateBoards(Set<Board> boards, String token) {
        Teacher teacher=repository.findByPhone(getMobileByToken(token))
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        teacher.setBoards(boards);
        repository.save(teacher);
        return ApiResponse.builder()
                .status(true)
                .message("Boards updated successfully")
                .build();
    }

    @Override
    public ApiResponse updateSubjects(Set<Subject> subjects, String token) {
        Teacher teacher=repository.findByPhone(getMobileByToken(token))
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        teacher.setSubjects(subjects);
        repository.save(teacher);
        return ApiResponse.builder()
                .status(true)
                .message("Subjects updated successfully")
                .build();
    }

    @Override
    public ApiResponse updateLanguage(Set<Language> languages, String token) {
        Teacher teacher=repository.findByPhone(getMobileByToken(token))
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        teacher.setLanguages(languages);
        repository.save(teacher);
        return ApiResponse.builder()
                .status(true)
                .message("Languages updated successfully")
                .build();
    }


    public String getMobileByToken(String token){
        token=token.substring(7);
        return jwtService.extractUsername(token);
    }


}
