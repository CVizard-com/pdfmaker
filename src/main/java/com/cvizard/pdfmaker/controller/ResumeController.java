package com.cvizard.pdfmaker.controller;

import com.cvizard.pdfmaker.model.Resume;
import com.cvizard.pdfmaker.repository.ResumeRepository;
import com.cvizard.pdfmaker.service.ResumeService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

import static com.cvizard.pdfmaker.model.ResumeStatus.ERROR;
import static com.cvizard.pdfmaker.model.ResumeStatus.READY;

@RestController
@RequestMapping("/api/maker")
@AllArgsConstructor
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final ResumeService resumeService;

    @GetMapping(path = "/download/{template}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getPdfFile(@PathVariable("template") String template, @RequestParam(name = "key") String key) throws IOException, DocumentException {

        Resume resume = resumeRepository.findById(key).orElse(Resume.builder().status(ERROR).build());
        return resumeService.createResponse(resume, key, template);
    }
}