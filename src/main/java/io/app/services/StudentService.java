package io.app.services;

import io.app.dto.ApiResponse;
import io.app.dto.StudentDto;
import io.app.model.Batch;
import io.app.model.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentService {
    public ApiResponse studentRegistration(String authToken,
                                           StudentDto studentDto,
                                           Long batchId,
                                           MultipartFile profilePic) throws IOException;
    public List<StudentDto> allStudentByTeacher(String authToken);
}
