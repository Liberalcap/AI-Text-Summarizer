package com.example.summarizer.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class SummarizerService {

    // ✅ Extract text from PDF or DOCX
    public String extractText(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        if (fileName != null && fileName.endsWith(".pdf")) {
            try (PDDocument document = PDDocument.load(inputStream)) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        } else if (fileName != null && fileName.endsWith(".docx")) {
            try (XWPFDocument doc = new XWPFDocument(inputStream)) {
                StringBuilder text = new StringBuilder();
                List<XWPFParagraph> paragraphs = doc.getParagraphs();
                for (XWPFParagraph para : paragraphs) {
                    text.append(para.getText()).append("\n");
                }
                return text.toString();
            }
        } else {
            throw new IllegalArgumentException("Only PDF and DOCX files are supported.");
        }
    }

    // ✅ Dummy summarizer (replace with OpenAI/HuggingFace call)
    public String summarizeText(String text) {
        if (text.length() > 300) {
            return text.substring(0, 300) + "... [Summary trimmed]";
        } else {
            return text + " [No summarization needed]";
        }
    }
}
