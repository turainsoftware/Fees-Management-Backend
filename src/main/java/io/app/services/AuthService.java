package io.app.services;


import io.app.dto.ApiResponse;
import io.app.dto.ResponseToken;
import io.app.model.Teacher;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    public ApiResponse login(String phoneNumber);
    public ApiResponse signup(Teacher teacher, MultipartFile profilePic) throws IOException;
    public ApiResponse signup(Teacher teacher);
    public ResponseToken validateToken(String phoneNumber,String otp);
}
