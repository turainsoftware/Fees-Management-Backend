package io.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.app.dto.ApiResponse;
import io.app.dto.StudentDto;
import io.app.model.Batch;
import io.app.model.Student;
import io.app.services.impl.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
    private final StudentServiceImpl service;

    @PostMapping("/registration")
    public ApiResponse createStudent(@RequestHeader("Authorization") String authToken,
                                     @RequestParam(name = "student") String student,
                                     @RequestParam(name = "batchId") Long batchId,
                                     @RequestParam(name = "profile") MultipartFile profilePic,
                                     @RequestParam("joiningYear") int joinYear,
                                     @RequestParam("joiningMonth") int joinMonth) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        System.out.println(joinYear+" "+joinMonth);
        StudentDto studentDto=objectMapper.readValue(student,StudentDto.class);
        return service.studentRegistration(authToken,studentDto,batchId,profilePic,joinYear,joinMonth);
    }

    @PatchMapping("/assign-batch")
    public ApiResponse assignStudentToABatch(@RequestHeader("Authorization") String authToken,
                                             @RequestParam("studentId") long studentId,
                                             @RequestParam("batchId") long batchId,
                                             @RequestParam("joiningYear") int startYear,
                                             @RequestParam("joiningMonth") int startMonth){
        System.out.println(startYear+" "+startMonth);
        return service.assignBatch(authToken,studentId,batchId,startYear,startMonth);
    }

    @GetMapping("/students")
    public List<StudentDto> allStudents(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(name = "isRecent",required = false,
                    defaultValue = "false") boolean isRecent){
        return service.allStudentByTeacher(authToken,isRecent);
    }

    @GetMapping("/batch")
    public List<StudentDto> studentsByBatches(@RequestParam("batchId") Long batchId){
        return service.allStudentByBatch(batchId);
    }

    @GetMapping("/isStudentExist")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse studentChecking(@RequestParam("mobile") String mobileNumber){
        return service.isStudentExists(mobileNumber);
    }

    @GetMapping("/mobile/{mobile}")
    public ResponseEntity<StudentDto> findStudentByMobileNumber(@PathVariable("mobile") String mobile){
        return ResponseEntity.ok(service.getStudentByMobile(mobile));
    }

}
