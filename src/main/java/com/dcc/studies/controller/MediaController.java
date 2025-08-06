package com.dcc.studies.controller;

import com.dcc.studies.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final FileStorageService fileStorageService;
    private final String bucketName;
    private final String s3Endpoint;

    @Autowired
    public MediaController(FileStorageService fileStorageService,
                           @Value("${aws.s3.bucket-name}") String bucketName,
                           @Value("${aws.s3.endpoint}") String s3Endpoint) {
        this.fileStorageService = fileStorageService;
        this.bucketName = bucketName;
        this.s3Endpoint = s3Endpoint;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = "/media/" + bucketName + "/" + fileName;
        Map<String, String> response = new HashMap<>();
        response.put("location", fileDownloadUri);
        return ResponseEntity.ok(response);
    }
}