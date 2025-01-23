package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.model.Language;
import io.app.services.impl.LanguageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/language")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageServiceImpl service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createLanguage(@RequestParam("language") String name){
        return service.createLanguage(name);
    }

    @PostMapping("/create-multiple")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createLanguage(@RequestBody Set<Language> languages){
        return service.createLanguage(languages);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Language> getAllLanguages(){
        return service.getAllLanguage();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteById(@PathVariable("id") Long id){
        return service.deleteById(id);
    }

    @DeleteMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteByName(@RequestParam("language") String name){
        return service.deleteByName(name);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteAll(){
        return service.deleteAll();
    }

    @GetMapping("/autosuggest")
    @ResponseStatus(HttpStatus.OK)
    public List<Language> autoSuggest(@RequestParam("query") String name){
        return service.autoSuggest(name);
    }

}
