package com.charan.SpringBoot_Awss3.SpringBoot_Aws.services;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public interface ImageUploader {


    String uploadImage(MultipartFile image);

    List<String> allFiles();

    String preSignedUrl(String fileName);

    String getImageUrlByName(String fileName);

    String getDateOfExp(String fileName);




}
