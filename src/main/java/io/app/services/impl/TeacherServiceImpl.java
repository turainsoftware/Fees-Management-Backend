package io.app.services.impl;

import io.app.dto.AnalysisResponse;
import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.TeacherDto;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.*;
import io.app.model.Class;
import io.app.repository.BatchRepository;
import io.app.repository.TeacherRepository;
import io.app.services.JwtService;
import io.app.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository repository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final BatchRepository batchRepository;

    @Override
    public TeacherDto profile(String authToken) {
        String mobileNumber=getMobileByToken(authToken);
        Teacher teacher=repository.findByPhone(mobileNumber)
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

    @Override
    public Set<BatchDto> allBatch(String authToken) {
        String phone=getMobileByToken(authToken);
        Teacher teacher=repository.findByPhone(phone)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        List<Batch> batches=batchRepository.findByTeacher(teacher);
        Set<BatchDto> result=batches.stream().map((item)->{
            return modelMapper.map(item,BatchDto.class);
        }).collect(Collectors.toSet());
        return result;
    }

    @Override
    public AnalysisResponse getStudentAnalysis(String authToken) {
        String mobileNumber=getMobileByToken(authToken);
        long teacherId=repository.findIdByPhone(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));

        Calendar calendar=Calendar.getInstance();
        Date endDate=calendar.getTime();
        calendar.add(Calendar.MONTH,-1);
        Date startDate=calendar.getTime();

        long totalStudents=repository.countStudentsByTeacherId(teacherId);

        long currentMonthsStudents=repository.countStudentsByTeacherIdAndDateRange(
                teacherId,
                startDate,
                endDate
        );

        double percentage=0;
        if(totalStudents!=0){
            percentage = ((double) currentMonthsStudents / totalStudents) * 100;
        }
        String trend=percentage>=0?"Increased":"Decreased";

        return AnalysisResponse.builder()
                .trend(trend)
                .percentage(percentage)
                .current(totalStudents)
                .build();
    }


    public String getMobileByToken(String token){
        token=token.substring(7);
        return jwtService.extractUsername(token);
    }


}
