package io.app.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.app.dto.ApiResponse;
import io.app.dto.ResponseToken;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Teacher;
import io.app.repository.TeacherRepository;
import io.app.services.AuthService;
import io.app.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final TeacherRepository repository;
    private final JwtService jwtService;
    private final Cloudinary cloudinary;


    @Override
    public ApiResponse login(String phoneNumber) {
        String otp=generateOtp();
        Teacher teacher=repository.findByPhone(phoneNumber)
                .orElseThrow(()->new ResourceNotFoundException("Teacher is not registered"));
        teacher.setOtp(otp);
        teacher.setOtpExpiry(new Date(System.currentTimeMillis()+ 5 * 60 * 1000));
        repository.save(teacher);
        return ApiResponse.builder()
                .status(true)
                .message("Otp has generated successfully")
                .build();
    }

    @Override
    public ApiResponse signup(Teacher teacher, MultipartFile profilePic) throws IOException {
        boolean isTeacherExists=repository.existsByPhone(teacher.getPhone());
        if (isTeacherExists){
            throw new DuplicateFoundException("Teacher already exists");
        }

        String profilePicUrl=uploadProfilePic(profilePic);
        teacher.setProfilePic(profilePicUrl);

        repository.save(teacher);
        return ApiResponse.builder()
                .status(true)
                .message("User Registration successfully")
                .build();
    }

    @Override
    public ResponseToken validateToken(String phoneNumber, String otp) {
        Teacher teacher=repository.findByPhone(phoneNumber)
                .orElseThrow(()->new ResourceNotFoundException("Teacher is not registered"));
        boolean isOtpValid=otp.equals(teacher.getOtp());
        if (isOtpValid && teacher.getOtpExpiry().after(new Date())){
            String token=jwtService.generateToken(teacher);
            return ResponseToken.builder()
                    .status(true)
                    .token(token)
                    .build();
        }

        return ResponseToken.builder()
                .status(false)
                .build();
    }

    private String generateOtp(){
        Random random=new Random();
        String otp="";
        for(int i=0;i<5;i++){
            int randomNumber=random.nextInt(10);
            otp+=randomNumber;
        }
        return otp;
    }


    private String uploadProfilePic(MultipartFile profilePic) throws IOException {
        Map uploadResult=cloudinary.uploader().upload(profilePic.getBytes(), ObjectUtils.asMap());
        String imageUrl=uploadResult.get("url").toString();
        return imageUrl;
    }

}
