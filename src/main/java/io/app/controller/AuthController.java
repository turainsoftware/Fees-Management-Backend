package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.dto.ResponseToken;
import io.app.model.Teacher;
import io.app.services.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl service;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse login(@RequestParam(name = "mobile") String phoneNumber){
        return service.login(phoneNumber);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse signUp(@RequestBody Teacher teacher,
                              @RequestParam("profile-pic") MultipartFile profilePic){
        return service.signup(teacher,profilePic);
    }

    @GetMapping("/validate-otp")
    @ResponseStatus(HttpStatus.OK)
    public ResponseToken validateOtp(@RequestParam("mobile") String phoneNumber,
                                     @RequestParam(name = "otp") String otp){
        return service.validateToken(phoneNumber,otp);
    }


}
