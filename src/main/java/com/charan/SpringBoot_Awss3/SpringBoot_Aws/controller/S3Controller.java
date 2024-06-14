package com.charan.SpringBoot_Awss3.SpringBoot_Aws.controller;

import com.amazonaws.Response;
import com.charan.SpringBoot_Awss3.SpringBoot_Aws.Impl.S3ImageUploader;
import com.charan.SpringBoot_Awss3.SpringBoot_Aws.services.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
public class S3Controller {

    @Autowired
    private ImageUploader imageUploader;


    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file)
    {
        return ResponseEntity.ok(imageUploader.uploadImage(file));
    }

    @GetMapping
    public List<String> getAllFiles(){
       return  imageUploader.allFiles();
    }

    @GetMapping("/{fileName}")
    public String getImageUrl(@PathVariable("fileName") String fileName){

        return  imageUploader.getImageUrlByName(fileName);
    }

    @GetMapping("/expDate/{fileName}")
    public String getExpDateImageUrl(@PathVariable("fileName") String fileName){

        return imageUploader.getDateOfExp(fileName);
    }
}
