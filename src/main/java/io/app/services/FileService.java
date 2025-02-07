package io.app.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    public String uploadProfilePic(MultipartFile profilePic) throws IOException;
    public MultipartFile resizeImage(MultipartFile file) throws IOException;
    public InputStream getProfileImage(String imageName) throws FileNotFoundException;
}
