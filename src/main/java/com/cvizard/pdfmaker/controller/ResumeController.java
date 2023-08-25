package com.cvizard.pdfmaker.controller;

import com.cvizard.pdfmaker.model.Resume;
import com.cvizard.pdfmaker.repository.ResumeRepository;
import com.cvizard.pdfmaker.service.ResumeService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.logging.Logger;

import static com.cvizard.pdfmaker.model.ResumeStatus.ERROR;
import static com.cvizard.pdfmaker.model.ResumeStatus.READY;

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

        Resume resume = resumeRepository.findById(key).orElse(Resume.builder().status(ERROR).build());
        log.info("Resume: " + resume);
        return resumeService.createResponse(resume, key, template, fileFormat);
    }
}