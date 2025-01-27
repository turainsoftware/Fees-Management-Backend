package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.StudentDto;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Batch;
import io.app.model.Student;
import io.app.model.Teacher;
import io.app.repository.StudentRepository;
import io.app.repository.TeacherRepository;
import io.app.services.JwtService;
import io.app.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;
    private final TeacherRepository teacherRepository;
    private final JwtService jwtService;


    @Override
    public ApiResponse studentRegistration(String authToken, StudentDto studentDto, Long batchId, MultipartFile profilePic) {
        boolean isStudentExists=repository.existsByPhone(studentDto.getPhone());

        return null;
    }



    private String saveProfilePic(){
        return null;
    }

    private String extractTeacher(String authToken){
        String phone=jwtService.extractUsername(authToken);
        return phone;
    }
}
