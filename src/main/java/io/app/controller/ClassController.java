package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.model.Class;
import io.app.services.impl.ClassServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/class")
@RequiredArgsConstructor
public class ClassController {
    private final ClassServiceImpl service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createClass(@RequestParam("className") String className){
        return service.createClass(className);
    }

    @PostMapping("/create-multiple")
    public ApiResponse createClass(@RequestBody Set<Class> classes){
        return service.createClass(classes);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Class> getAllClass(){
        return service.getAllClass();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteById(@PathVariable Long id){
        return service.deleteClassById(id);
    }

    @DeleteMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteByName(@RequestParam("name") String name){
        return service.deleteClassByName(name);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteAll(){
        return service.deleteAll();
    }

    @GetMapping("/autoSuggest")
    @ResponseStatus(HttpStatus.OK)
    public List<Class> autoSuggest(@RequestParam("query") String query){
        return service.autoSuggest(query);
    }


}
