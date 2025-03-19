package io.app.services.impl;

import io.app.dto.AnalysisResponse;
import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.Projections.TeacherProjection;
import io.app.dto.TeacherDto;
import io.app.excetptions.NotAllowedException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.*;
import io.app.model.Class;
import io.app.repository.BatchRepository;
import io.app.repository.TeacherRepository;
import io.app.services.JwtService;
import io.app.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository repository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final BatchRepository batchRepository;
    private final FileServiceImpl fileService;

    @Override
    public TeacherDto profile(String authToken) {
        String mobileNumber=getMobileByToken(authToken);
        Teacher teacher=repository.findByPhone(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        TeacherDto teacherDto=modelMapper.map(teacher,TeacherDto.class);
        return teacherDto;
    }

    @Override
    public TeacherProjection profileLess(String authToken) {
        String mobile= getMobileByToken(authToken);
        TeacherProjection teacherDto=repository.findProfileLessByMobile(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
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
    public boolean teacherExistByMobileNumber(String mobileNumber) {
        boolean status=repository.existsByPhone(mobileNumber);
        return status;
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
    public Set<Subject> subjectsOfTeachers(String authToken) {
        String mobile=getMobileByToken(authToken);
        Teacher teacher=repository.findByPhone(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        return teacher.getSubjects();
    }

    @Override
    public AnalysisResponse getStudentAnalysis(String authToken) {
        String mobileNumber = getMobileByToken(authToken);
        long teacherId = repository.findIdByPhone(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Credentials"));

        // Get the current date and the start of the current month
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date
        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime(); // Start of the previous month

        // Get the start of the previous month
        calendar.add(Calendar.MONTH, -1);
        Date previousMonthStartDate = calendar.getTime();

        // Count students for the current month
        long currentMonthStudents = repository.countStudentsByTeacherIdAndDateRange(
                teacherId,
                startDate,
                endDate
        );

        // Count students for the previous month
        long previousMonthStudents = repository.countStudentsByTeacherIdAndDateRange(
                teacherId,
                previousMonthStartDate,
                startDate
        );

        // Calculate the percentage change
        double percentage;
        if (previousMonthStudents == 0) {
            // If there were no students in the previous month, consider it as a 100% increase or 0% if no students in the current month
            percentage = currentMonthStudents > 0 ? 100 : 0;
        } else {
            percentage = ((double) (currentMonthStudents - previousMonthStudents) / previousMonthStudents) * 100;
        }

        // Round the percentage to 2 decimal places
        percentage = Math.round(percentage * 100.0) / 100.0;

        // Determine the trend
        String trend = percentage >= 0 ? "Increased" : "Decreased";

        return AnalysisResponse.builder()
                .trend(trend)
                .percentage(percentage)
                .current(currentMonthStudents) // Current month's student count
                .build();
    }

    @Override
    public AnalysisResponse getSubjectAnalysis(String authToken) {
        String mobileNumber=getMobileByToken(authToken);
        long teacherId=repository.findIdByPhone(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Teacher Credentials"));

        long totalSubjects=repository.countSubjectsByTeacherId(teacherId);


        return AnalysisResponse.builder()
                .current(totalSubjects)
                .trend("Increased")
                .percentage(100)
                .build();
    }

    @Override
    public ApiResponse updateProfile(String authToken, String name,
                                     String email, Gender gender) {
        String mobile=getMobileByToken(authToken);
        Teacher teacher=repository.findByPhone(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        teacher.setName(name);
        if(email!=null && !email.isEmpty()){
            teacher.setEmail(email);
        }

        if(gender!=null){
            teacher.setGender(gender);
        }

        repository.save(teacher);

        return ApiResponse.builder()
                .message("Updated Successfully")
                .status(true)
                .build();
    }

        @Override
        @Transactional
        public ApiResponse updateProfilePicture(String authToken,
                                                MultipartFile profileImage) throws IOException {
            String mobile=getMobileByToken(authToken);
            Teacher teacher=repository.findByPhone(mobile)
                    .orElseThrow(()->new ResourceNotFoundException("Invalid Request"));
            ApiResponse apiResponse=ApiResponse.builder()
                    .status(false)
                    .build();
            if(fileService.deleteProfilePicture(teacher.getProfilePic())){
                String profileUrl=fileService.uploadProfilePic(profileImage);
                teacher.setProfilePic(profileUrl);
                apiResponse.setMessage(profileUrl);
                apiResponse.setStatus(true);
            }else{
                throw new NotAllowedException("Something went wrong!");
            }


            return apiResponse;
        }

    @Override
    public Set<Language> languages(String authToken) {
        String mobile=getMobileByToken(authToken);
        Teacher teacher=repository.findByPhone(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        return teacher.getLanguages();
    }

    @Override
    public Set<Board> boards(String authToken) {
        String mobile=getMobileByToken(authToken);
        Teacher teacher=repository.findByPhone(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        return teacher.getBoards();
    }

    @Override
    public Set<Class> classes(String authToken) {
        String mobile=getMobileByToken(authToken);
        Teacher teacher=repository.findByPhone(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        return teacher.getClasses();
    }


    public String getMobileByToken(String token){
        token=token.substring(7);
        return jwtService.extractUsername(token);
    }


}
