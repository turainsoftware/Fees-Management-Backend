package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.model.Batch;
import io.app.model.Student;
import io.app.services.impl.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentServiceImpl service;

    @PostMapping("/registration")
    public ApiResponse createStudent(@RequestHeader("Authorization") String authToken,
                                     @RequestBody Student student,
                                     @RequestBody Batch batch,
                                     @RequestParam MultipartFile profilePic){
//        return service.studentRegistration(authToken,student,batch);
        return null;
    }


}
