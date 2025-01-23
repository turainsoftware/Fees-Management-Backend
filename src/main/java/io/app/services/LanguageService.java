package io.app.services;

import io.app.dto.ApiResponse;
import io.app.model.Language;

import java.util.List;
import java.util.Set;

public interface LanguageService {
    public ApiResponse createLanguage(String language);
    public ApiResponse createLanguage(Set<Language> languages);
    public List<Language> getAllLanguage();
    public ApiResponse deleteById(Long id);
    public ApiResponse deleteByName(String name);
    public ApiResponse deleteAll();
    public List<Language> autoSuggest(String name);
}
