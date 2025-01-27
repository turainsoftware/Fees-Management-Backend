package io.app.services;

import io.app.dto.ApiResponse;
import io.app.dto.StudentDto;
import io.app.model.Batch;
import io.app.model.Student;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    public ApiResponse studentRegistration(String authToken,
                                           StudentDto studentDto,
                                           Long batchId,
                                           MultipartFile profilePic);

}
