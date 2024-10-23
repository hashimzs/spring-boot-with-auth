package com.ga.basic_auth.util;

import com.ga.basic_auth.model.ImageDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtil {

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    public ImageDetails handleFileUpload(MultipartFile file) {
        try {
            // Generate a random file name using UUID
            String fileName = UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());

            // Save the file to the upload directory
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, bytes);

            // Create an instance of the ImageDetails class
            ImageDetails imageDetails = new ImageDetails(
                    "http://localhost:8080/" + fileName,
                    fileName,
                    file.getContentType()
            );

            return imageDetails;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        return lastIndexOfDot > 0 ? fileName.substring(lastIndexOfDot + 1) : "";
    }

    public void deleteImage(String fileName) throws IOException {
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.deleteIfExists(imagePath);
    }


}
