package io.app.services;


import io.app.dto.ApiResponse;
import io.app.dto.ResponseToken;
import io.app.model.Teacher;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    public ApiResponse login(String phoneNumber);
    public ApiResponse signup(Teacher teacher, MultipartFile profilePic);
    public ResponseToken validateToken(String phoneNumber,String otp);
}
