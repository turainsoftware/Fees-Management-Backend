package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.TeacherDto;
import io.app.model.*;
import io.app.model.Class;
import io.app.services.impl.TeacherServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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


}
