package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.BatchEndYearMonthProjection;
import io.app.dto.StudentDto;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.NotAllowedException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.*;
import io.app.repository.*;
import io.app.services.JwtService;
import io.app.services.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ResourceClosedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;
    private final TeacherRepository teacherRepository;
    private final JwtService jwtService;
    private final FileServiceImpl fileService;
    private final BatchRepository batchRepository;
    private final ModelMapper modelMapper;
    private final StudentBatchEnrollmentRepository studentBatchEnrollmentRepository;
    private final FeesRepository feesRepository;


    private final static long MAX_PICSIZE=200*1024;


    @Override
    public ApiResponse studentRegistration(String authToken,
                                           StudentDto studentDto,
                                           Long batchId,
                                           MultipartFile profilePic,
                                           int joiningYear,
                                           int joiningMonth) throws IOException {
        if (profilePic.getSize()>MAX_PICSIZE){
            throw new NotAllowedException("Image size should be less than 200KB");
        }
        // Student Validation
//        boolean isStudentExist=repository.existsByPhone(studentDto.getPhone());
//        if (isStudentExist){
//            throw new DuplicateFoundException("Student Already exists do Assign a batch");
//        }

        // Teacher Validation
        String teacherPhone=extractTeacher(authToken);
        Teacher teacher=teacherRepository.findByPhone(teacherPhone)
                .orElseThrow(()->new ResourceNotFoundException("Invalid teacher credentials"));

        //previousOne
        Batch batch=Batch.builder()
                .id(batchId)
                .build();
//        Updated one
        BatchEndYearMonthProjection projection=batchRepository.findEndYearMonthById(batchId)
                .orElseThrow(()->new ResourceNotFoundException("Invalid batch"));

        // Batch Validation
//        Set<Batch> batches = teacher.getBatches().stream().filter((item)->{
//            return item.getId().equals(batchId);
//        }).collect(Collectors.toSet());
//
//        if(batches.size()<=0){
//            throw new ResourceNotFoundException("Teacher doesn't have such Batch");
//        }

//        if (!teacher.getBatches().contains(batch)){
//            throw new ResourceNotFoundException("Teacher doesn't have such Batch");
//        }

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

        student.setBatches(Set.of(batch));

        Student savedStudent = repository.save(student);

//        StudentBatchEnrollment enrollment=StudentBatchEnrollment.builder()
//                .year(joiningYear)
//                .month(joiningMonth)
//                .student(savedStudent)
//                .batch(batch)
//                .build();
//        studentBatchEnrollmentRepository.save(enrollment);

        Fees fees=Fees.builder()
                .batch(batch)
                .student(student)
                .startYear(joiningYear)
                .startMonth(joiningMonth)
                .endMonth(projection.endMonth())
                .endYear(projection.endYear())
                .monthlyFees(projection.monthlyFees())
                .build();
        feesRepository.save(fees);

        return ApiResponse.builder()
                .status(true)
                .message("Student Registration Successfully Completed")
                .build();
    }

    @Override
    public ApiResponse assignBatch(String authToken,
                                   long studentId,
                                   long batchId,
                                   int startYear,
                                   int startMonth) {
        String mobileNumber=extractTeacher(authToken);
        Long teacherId=teacherRepository.findIdByPhone(mobileNumber)
                .orElseThrow(()->new ResourceClosedException("Invalid credentials"));

        Batch batch=Batch.builder()
                .id(batchId)
                .build();
        BatchEndYearMonthProjection projection=batchRepository.findEndYearMonthById(batchId)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Batch!"));

        System.out.println(projection.endMonth()+" "+projection.endYear());

        Teacher teacher=Teacher.builder()
                .id(teacherId)
                .build();
        Student student=repository.findById(studentId)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Student"));
        Set<Teacher> updatedTeachers=student.getTeachers();
        updatedTeachers.add(teacher);
        Set<Batch> updatedBatch=student.getBatches();
        updatedBatch.add(batch);
        if (isStudentAlreadyEnrolledTheBatch(batch,studentId)){
            throw new DuplicateFoundException("Student already exist in the batch");
        }
        Student savedStudent=repository.save(student);

//        StudentBatchEnrollment studentBatchEnrollment=StudentBatchEnrollment.builder()
//                .batch(batch)
//                .student(savedStudent)
//                .year(startYear)
//                .month(startMonth)
//                .build();
//        studentBatchEnrollmentRepository.save(studentBatchEnrollment);

        Fees fees=Fees.builder()
                .startMonth(startMonth)
                .startYear(startYear)
                .endYear(projection.endYear())
                .endMonth(projection.endMonth())
                .batch(batch)
                .student(savedStudent)
                .monthlyFees(projection.monthlyFees())
                .build();

        feesRepository.save(fees);

        return ApiResponse.builder()
                .status(true)
                .message("Student assign successfully completed")
                .build();
    }

    @Override
    public List<StudentDto> allStudentByTeacher(String authToken,boolean isRecent) {
        String phone=extractTeacher(authToken);
        Teacher teacher=teacherRepository.findByPhone(phone)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        List<Student> students;
        if (isRecent){
            students=repository.findByTeachersOrderByCreatedAtDesc(teacher);
        }else{
            students=repository.findByTeachersOrderByCreatedAtAsc(teacher);
        }
        List<StudentDto> result=students.stream().map((item)->{
            return modelMapper.map(item,StudentDto.class);
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<StudentDto> allStudentByBatch(Long batchId) {
        boolean isBatchExists=batchRepository.existsById(batchId);
        Batch batch;
        if(isBatchExists){
            batch=Batch.builder()
                    .id(batchId)
                    .build();
        }else{
             throw new ResourceNotFoundException("Invalid Batch Selected");
        }

        List<Student> students=repository.findByBatches(Set.of(batch));
        List<StudentDto> result=students.stream().map((item)->{
            return modelMapper.map(item, StudentDto.class);
        }).collect(Collectors.toList());

        return result;
    }

    @Override
    public ApiResponse isStudentExists(String mobileNumber) {
        boolean isStudentExist=repository.existsByPhone(mobileNumber);
        if (isStudentExist){
            return ApiResponse.builder()
                    .status(true)
                    .build();
        }
        return ApiResponse.builder()
                .status(false)
                .build();
    }

    @Override
    public StudentDto getStudentByMobile(String mobile) {
        Student student=repository.findByPhone(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Student Not found"));
        StudentDto studentDto=modelMapper.map(student,StudentDto.class);
        return studentDto;
    }

    private String extractTeacher(String authToken){
        authToken=authToken.substring(7);
        String phone=jwtService.extractUsername(authToken);
        return phone;
    }

    private boolean isStudentAlreadyEnrolledTheBatch(Batch batch,long studentId){
        List<Student> batches=repository.findByBatches(Set.of(batch));
        return batches.stream().anyMatch((item)->item.getId().equals(studentId));
    }
}