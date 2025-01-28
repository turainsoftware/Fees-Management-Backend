package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.StudentDto;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.NotAllowedException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Batch;
import io.app.model.Student;
import io.app.model.Teacher;
import io.app.repository.BatchRepository;
import io.app.repository.StudentRepository;
import io.app.repository.TeacherRepository;
import io.app.services.JwtService;
import io.app.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;
    private final TeacherRepository teacherRepository;
    private final JwtService jwtService;
    private final FileServiceImpl fileService;
    private final BatchRepository batchRepository;
    private final ModelMapper modelMapper;

    private final static long MAX_PICSIZE=200*1024;


    @Override
    public ApiResponse studentRegistration(String authToken,
                                           StudentDto studentDto,
                                           Long batchId,
                                           MultipartFile profilePic) throws IOException {

        if (profilePic.getSize()>MAX_PICSIZE){
            throw new NotAllowedException("Image size should be less than 200KB");
        }
        // Student Validation
        boolean isStudentExist=repository.existsByPhone(studentDto.getPhone());
        if (isStudentExist){
            throw new DuplicateFoundException("Student Already exists do Assign a batch");
        }

        // Teacher Validation
        String teacherPhone=extractTeacher(authToken);
        Teacher teacher=teacherRepository.findByPhone(teacherPhone)
                .orElseThrow(()->new ResourceNotFoundException("Invalid teacher credentials"));

        // Batch Validation
        Set<Batch> batches = teacher.getBatches().stream().filter((item)->{
            return item.getId().equals(batchId);
        }).collect(Collectors.toSet());

        if(batches.size()<=0){
            throw new ResourceNotFoundException("Teacher doesn't have such Batch");
        }

        String profileUrl=fileService.uploadProfilePic(profilePic);
        Student student=modelMapper.map(studentDto,Student.class);
        student.setProfilePic(profileUrl);

        // Associating Teacher with Student
        Set<Teacher> teachers=new HashSet<>();
        teachers.add(teacher);
        student.setTeachers(teachers);

        //Associating Batch with Student
//        Set<Batch> batches = new HashSet<>();
//        batches.add(batch);
        student.setBatches(batches);

        Student savedStudent =repository.save(student);
        return ApiResponse.builder()
                .status(true)
                .message("Student Registration Successfully Completed")
                .build();
    }

    @Override
    public List<StudentDto> allStudentByTeacher(String authToken) {
        String phone=extractTeacher(authToken);
        Teacher teacher=teacherRepository.findByPhone(phone)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        List<Student> students=repository.findByTeachers(teacher);
        List<StudentDto> result=students.stream().map((item)->{
            return modelMapper.map(item,StudentDto.class);
        }).collect(Collectors.toList());
        return result;
    }

    private String extractTeacher(String authToken){
        authToken=authToken.substring(7);
        String phone=jwtService.extractUsername(authToken);
        return phone;
    }
}