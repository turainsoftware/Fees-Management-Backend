package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Language;
import io.app.repository.LanguageRepository;
import io.app.services.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository repository;

    @Override
    public ApiResponse createLanguage(String language) {
        boolean isExists=repository.existsByName(language);
        if(isExists){
            throw new DuplicateFoundException("Language Already Exists");
        }
        Language dbLanguage=Language.builder()
                .name(language)
                .build();
        repository.save(dbLanguage);
        return ApiResponse.builder()
                .status(true)
                .message("Language Created Successfully")
                .build();
    }

    @Override
    public ApiResponse createLanguage(Set<Language> languages) {
        repository.saveAll(languages);
        return ApiResponse.builder()
                .message("Added all the languages")
                .status(true)
                .build();
    }

    @Override
    public List<Language> getAllLanguage() {
        return repository.findAll();
    }

    @Override
    public ApiResponse deleteById(Long id) {
        repository.deleteById(id);
        return ApiResponse.builder()
                .status(true)
                .message("Language Deleted Successfully")
                .build();
    }

    @Override
    public ApiResponse deleteByName(String name) {
        Language language=repository.findByName(name)
                .orElseThrow(()->new ResourceNotFoundException("Language not exists"));
        return ApiResponse.builder()
                .message("Language Deleted Successfully")
                .status(true)
                .build();
    }

    @Override
    public ApiResponse deleteAll() {
        repository.deleteAll();
        return ApiResponse.builder()
                .message("All Languages Deleted Successfully")
                .status(true)
                .build();
    }

    @Override
    public List<Language> autoSuggest(String name) {
        List<Language> results=repository.findByNameContaining(name);
        return results;
    }


}
