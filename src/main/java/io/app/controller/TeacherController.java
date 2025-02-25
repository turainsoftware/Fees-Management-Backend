package io.app.controller;

import io.app.dto.AnalysisResponse;
import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.Projections.TeacherProjection;
import io.app.dto.TeacherDto;
import io.app.model.*;
import io.app.model.Class;
import io.app.services.impl.TeacherServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherServiceImpl service;

    @GetMapping("/profile")
    public ResponseEntity<TeacherDto> profile(@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(service.profile(authHeader));
    }

    @GetMapping("/only-profile")
    public ResponseEntity<TeacherProjection> profileLess(
            @RequestHeader("Authorization") String authHeader
    ){
        return ResponseEntity.ok(service.profileLess(authHeader));
    }

    @PatchMapping("/class/update-classes")
    public ResponseEntity<ApiResponse> updateClasses(@RequestBody Set<Class> classes,
                                                     @RequestHeader("Authorization") String authToken){
        return ResponseEntity.ok(service.updateClasses(classes,authToken));
    }

    @PatchMapping("/board/update-boards")
    public ResponseEntity<ApiResponse> updateBoard(@RequestBody Set<Board> boards,
                                                     @RequestHeader("Authorization") String authToken){
        return ResponseEntity.ok(service.updateBoards(boards,authToken));
    }

    @PatchMapping("/subject/update-subjects")
    public ResponseEntity<ApiResponse> updateSubjects(@RequestBody Set<Subject> subjects,
                                                     @RequestHeader("Authorization") String authToken){
        return ResponseEntity.ok(service.updateSubjects(subjects,authToken));
    }

    @PatchMapping("/language/update-languages")
    public ResponseEntity<ApiResponse> updateLanguages(@RequestBody Set<Language> languages,
                                                     @RequestHeader("Authorization") String authToken){
        return ResponseEntity.ok(service.updateLanguage(languages,authToken));
    }

    @GetMapping("/batches")
    @ResponseStatus(HttpStatus.OK)
    public Set<BatchDto> allBatches(@RequestHeader("Authorization") String authToken){
        return service.allBatch(authToken);
    }


    @GetMapping("/student-analysis")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisResponse studentAnalyse(
            @RequestHeader("Authorization") String authToken){
        return service.getStudentAnalysis(authToken);
    }

    @GetMapping("/subject-analysis")
    public AnalysisResponse subjectsAnalysis(
            @RequestHeader("Authorization") String authToken){
        return service.getSubjectAnalysis(authToken);
    }

    @GetMapping("/check-by-mobile")
    public boolean teacherExistByMobileNumber(@RequestParam("mobile") String mobile){
        return service.teacherExistByMobileNumber(mobile);
    }



    /*
        *** Update Profile -- only{name,email,gender}
     */
    @PutMapping("/profile/update-profile")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse updateProfile(
            @RequestHeader("Authorization") String authToken,
            @RequestParam("name") String name,
            @RequestParam(value = "email",required = false) String email,
            @RequestParam("gender") Gender gender){
        return service.updateProfile(authToken,name,email,gender);
    }

    @GetMapping("/subjects")
    public Set<Subject> subjectsOfTeacher(
            @RequestHeader("Authorization") String authToken
    ){
        return service.subjectsOfTeachers(authToken);
    }


}
