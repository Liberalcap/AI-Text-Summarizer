package com.example.summarizer.controller;

import com.example.summarizer.service.SummarizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SummarizerController {

    @Autowired
    private SummarizerService summarizerService;

    @PostMapping("/summarize")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Extract text from file (PDF or DOCX)
            String extractedText = summarizerService.extractText(file);

            // Summarize the extracted text
            return summarizerService.summarizeText(extractedText);

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
