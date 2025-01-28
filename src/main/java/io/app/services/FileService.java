package io.app.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public String uploadProfilePic(MultipartFile profilePic) throws IOException;
}
