package io.app.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.app.services.FileService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
//    private final Cloudinary cloudinary;

    @Value("${file.upload-dir.profile}")
    private String uploadDirectory;

    @Override
    public String uploadProfilePic(MultipartFile profilePic) throws IOException {
        File file=new File(uploadDirectory);
        if (!file.exists()){
            file.mkdirs();
        }
        MultipartFile profile=resizeImage(profilePic);
        String extension=profile.getOriginalFilename().
                substring(profile.getOriginalFilename().lastIndexOf("."));

        String imageName= UUID.randomUUID().toString()+extension;
        try(FileOutputStream fileOutputStream=new FileOutputStream(uploadDirectory+File.separator+imageName)){
            fileOutputStream.write(profile.getBytes());
        }


        return imageName;
    }

    @Override
    public MultipartFile resizeImage(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        BufferedImage originalImage = ImageIO.read(inputStream);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage processedImage = originalImage;

        // Check if the image is not in 1:1 aspect ratio
        if (width != height) {
            int size = Math.min(width, height);
            int x = (width - size) / 2;
            int y = (height - size) / 2;

            processedImage = originalImage.getSubimage(x, y, size, size);
        }

        // Determine the target size based on the original dimensions
        int targetSize = (width < 300 && height < 300) ? 200 : 300;

        // Resize the image to the target size
        BufferedImage resizedImage = Thumbnails.of(processedImage)
                .size(targetSize, targetSize)
                .asBufferedImage();

        // Compress the image to ensure it is within 150KB
        float quality = 1.0f;
        do {
            outputStream.reset();
            ImageIO.write(resizedImage, "jpg", outputStream);
            if (outputStream.size() <= 150 * 1024) {
                break;
            }
            quality -= 0.1f;
            Thumbnails.of(resizedImage)
                    .size(targetSize, targetSize)
                    .outputQuality(quality)
                    .toOutputStream(outputStream);
        } while (quality > 0.1f);

        return new MockMultipartFile(
                file.getName(),
                file.getOriginalFilename(),
                "image/jpeg",
                outputStream.toByteArray()
        );
    }


    @Override
    public InputStream getProfileImage(String imageName) throws FileNotFoundException {
        FileInputStream fileInputStream=new FileInputStream(uploadDirectory+File.separator+imageName);
        return fileInputStream;
    }


//    @Override
//    public String uploadProfilePic(MultipartFile profilePic) throws IOException {
//        String transformation = "c_fill,w_500,h_500,g_faces";
//
//
//        Map uploadResult=cloudinary.uploader()
//                .upload(profilePic.getBytes(),
//                        ObjectUtils.asMap("transformation",transformation));
//        String imageUrl=uploadResult.get("url").toString();
//        return imageUrl;
//    }


}
