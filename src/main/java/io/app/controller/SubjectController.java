package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.model.Subject;
import io.app.services.impl.SubjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectServiceImpl service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createSubject(@RequestParam("subject") String subjectName){
        return service.createSubject(subjectName);
    }

    @PostMapping("/create-multiple")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createSubject(@RequestBody Set<Subject> subjects){
        return service.createSubject(subjects);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> allSubjects(){
        return service.getAllSubjects();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteById(@PathVariable("id") Long id){
        return service.deleteSubjectsById(id);
    }


    @DeleteMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteByName(@RequestParam("name") String name){
        return service.deleteSubjectsByName(name);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteAll(){
        return service.deleteAll();
    }

    @GetMapping("/autoSuggest")
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> autoSuggest(@RequestParam("query") String query){
        return service.autoSuggest(query);
    }

}
