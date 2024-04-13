package ru.mal.unialbumsbackend.service;

import io.minio.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.exception.ImageUploadException;

import java.io.InputStream;
import java.util.UUID;

@Service
public class ImageService {

    private final MinioClient minioClient;

    public ImageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String upload(MultipartFile image){
        try{
            createBucket();
        }
        catch (Exception e) {
            throw new ImageUploadException("Загрузка изображения не прошла("+e.getMessage());
        }
        if(image.isEmpty() || image.getOriginalFilename()==null){
            throw new ImageUploadException("У изображения должно быть название");
        }
        String filename=generateFileName(image);
        InputStream inputStream;
        try{
            inputStream= image.getInputStream();
        }
        catch (Exception e){
            throw new ImageUploadException("Загрузка изображения не прошла("+e.getMessage());
        }
        saveImage(inputStream,filename);
        return filename;
    }

    @SneakyThrows
    private void createBucket(){
        boolean found=minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket("images")
                .build());
        if(!found){
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket("images")
                    .build());
        }

    }

    private String generateFileName(MultipartFile file){
        String extension=getExtension(file);
        return UUID.randomUUID()+"."+extension;
    }
    private String getExtension(MultipartFile file){
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream,String fileName){
        ObjectWriteResponse response= minioClient.putObject(PutObjectArgs.builder()
                        .stream(inputStream, inputStream.available(), -1)
                        .bucket("images")
                        .object(fileName)
                .build());}

}
