package io.app.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.app.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;


    @Override
    public String uploadProfilePic(MultipartFile profilePic) throws IOException {
        String transformation = "c_fill,w_500,h_500,g_faces";


        Map uploadResult=cloudinary.uploader()
                .upload(profilePic.getBytes(),
                        ObjectUtils.asMap("transformation",transformation));
        String imageUrl=uploadResult.get("url").toString();
        return imageUrl;
    }
}
