package com.charan.SpringBoot_Awss3.SpringBoot_Aws.exception;

import org.springframework.stereotype.Component;

import java.io.IOException;


public class ImageUploadException extends RuntimeException{

    public ImageUploadException(String message){

        super(message);


    }


}
