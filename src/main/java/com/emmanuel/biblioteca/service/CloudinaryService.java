package com.emmanuel.biblioteca.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        // Redimensionar la imagen a 600x300 sin perder calidad ni recortar
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(400, 700)  // Redimensionar manteniendo la proporción
                .keepAspectRatio(true) // Mantener la proporción
                .outputFormat("jpg") // Establecer el formato de salida (puedes usar "png" si prefieres)
                .toOutputStream(baos);

        byte[] resizedImage = baos.toByteArray();

        // Subir la imagen redimensionada a Cloudinary
        Map uploadResult = cloudinary.uploader().upload(resizedImage, ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }
}