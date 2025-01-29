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
                                     @RequestParam(name = "profile") MultipartFile profilePic) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        log.info("Entered in registration");
        StudentDto studentDto=objectMapper.readValue(student,StudentDto.class);
        return service.studentRegistration(authToken,studentDto,batchId,profilePic);
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


}
