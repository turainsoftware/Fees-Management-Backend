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
                                           MultipartFile profilePic,
                                           int joiningYear,
                                           int joiningMonth) throws IOException;
    public ApiResponse studentRegistration(String authToken,
                                           StudentDto studentDto,
                                           Long batchId,
                                           int joiningYear,
                                           int joiningMonth) throws IOException;
    public ApiResponse assignBatch(String authToken,long studentId,long batchId,int startYear,int startMonth);
    public List<StudentDto> allStudentByTeacher(String authToken,boolean isRecent);
    public List<StudentDto> allStudentByBatch(Long batchId);
    public ApiResponse isStudentExists(String mobileNumber);
    public StudentDto getStudentByMobile(String mobile);
}
