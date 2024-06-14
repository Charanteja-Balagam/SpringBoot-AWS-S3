package com.charan.SpringBoot_Awss3.SpringBoot_Aws.Impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.charan.SpringBoot_Awss3.SpringBoot_Aws.exception.ImageUploadException;
import com.charan.SpringBoot_Awss3.SpringBoot_Aws.services.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class S3ImageUploader implements ImageUploader {


    @Autowired

    private AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketName;
    @Override
    public String uploadImage(MultipartFile image) throws ImageUploadException {

        if(image == null){
            throw new ImageUploadException("Image is null");
        }

        String actualFileName = image.getOriginalFilename();

        String fileName=    UUID.randomUUID().toString()+ actualFileName.substring(actualFileName.lastIndexOf("."));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(image.getSize());
        try {
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName,fileName,image.getInputStream(), objectMetadata));
            return this.preSignedUrl(fileName);
        } catch (IOException e) {
            throw new ImageUploadException("error in uploading image" +e.getMessage());
        }



    }

    @Override
    public List<String> allFiles() {

        ListObjectsV2Result listObjectsV2Result = client.listObjectsV2(new ListObjectsV2Request().withBucketName(bucketName));
        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
       List<String> listFileUrls =  objectSummaries.stream().map(item ->this.preSignedUrl(item.getKey())).collect(Collectors.toList());
        return listFileUrls;
    }

    @Override
    public String preSignedUrl(String fileName) {
        Date expiratonDate = new Date();
        long time = expiratonDate.getTime();
        int hour=2;
        time = time + hour *60*60*1000;
        expiratonDate.setTime(time);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                        .withExpiration(expiratonDate);
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    @Override
    public String getImageUrlByName(String fileName) {

        S3Object object = client.getObject(bucketName,fileName);
        String key = object.getKey();
        String url = preSignedUrl(key);
        return url;
    }

    public String getDateOfExp(String fileName){

        S3Object object = client.getObject(bucketName,fileName);

       String url = preSignedUrl(fileName);
        String regex = "X-Amz-Date=(\\d{8})T";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        String date = null;
        if (matcher.find()) {
            date = matcher.group(1);
        }

        System.out.println(date);
        return date;

    }
}
