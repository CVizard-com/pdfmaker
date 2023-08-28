package com.cvizard.pdfmaker.controller;

import com.cvizard.pdfmaker.model.Resume;
import com.cvizard.pdfmaker.repository.ResumeRepository;
import com.cvizard.pdfmaker.service.ResumeService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@RestController
@RequestMapping("/api/maker")
@AllArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final ResumeService resumeService;

    @GetMapping(path = "/download")
    public ResponseEntity<?> getPdfFile(
            @RequestParam("template") String template,
            @RequestParam(name = "key") String key,
            @RequestParam("format") String fileFormat ) throws IOException, DocumentException {

        Resume resume = resumeRepository.findById(key).orElseThrow(() -> new RuntimeException("Resume not found"));
        log.info("Resume found: " + resume);
        return resumeService.createResponse(resume, key, template, fileFormat);
    }
}