package io.app.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.app.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

        // If the image is already 1:1 and ≤ 150KB, return it as is
        if (width == height && file.getSize() <= (150 * 1024)) {
            return file;
        }

        BufferedImage processedImage = originalImage;

        // Crop if not in 1:1 aspect ratio
        if (width != height) {
            int size = Math.min(width, height);
            int x = (width - size) / 2;
            int y = (height - size) / 2;
            processedImage = originalImage.getSubimage(x, y, size, size);
        }

        // If the image was cropped (not originally 1:1), determine the target size
        BufferedImage finalImage = processedImage;
        if (width != height) {
            int targetSize = (width < 300 && height < 300) ? 200 : 300;
            finalImage = Thumbnails.of(processedImage)
                    .size(targetSize, targetSize)
                    .asBufferedImage();
        }

        // Determine the original file format and content type
        String originalContentType = file.getContentType();
        String originalFormat = originalContentType != null && originalContentType.contains("png") ? "png" : "jpg";
        // Convert to output stream
        ImageIO.write(finalImage, originalFormat, outputStream);

        // If size is already ≤ 150KB, return it as is
        if (outputStream.size() <= 150 * 1024) {
            return new MockMultipartFile(
                    file.getName(),
                    file.getOriginalFilename(),
                    "image/jpeg",
                    outputStream.toByteArray()
            );
        }

        // Compress the image to ensure it's within 150KB
        float quality = 1.0f;
        do {
            outputStream.reset();
            Thumbnails.of(finalImage)
                    .outputQuality(quality)
                    .toOutputStream(outputStream);
            if (outputStream.size() <= 150 * 1024) {
                break;
            }
            quality -= 0.1f;
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

    @Override
    public boolean deleteProfilePicture(String profilePictureName) {
        if(profilePictureName.contains("default")){
            return true;
        }
        boolean flag=true;

        File file=new File(uploadDirectory+ File.separator+profilePictureName);
        System.out.println(uploadDirectory+profilePictureName);
        if(file.exists()){
            return file.delete();
        }else{
            flag=false;
        }
        return flag;
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
