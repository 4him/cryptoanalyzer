package com.assignment.cryptoanalyzer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/data")
public class FileProcessingController {

    // NOT WORKING!!! This one could be implemented as feature to upload files and save data to DB as advice for scalabale application logic
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCryptoData (@RequestParam("file") MultipartFile file) {
        // sample and all logic to implement uploading goes here
        return ResponseEntity.ok("All data from file was successfully upload in DB and updated");
    }
}
