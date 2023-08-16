package com.cvizard.pdfcreator.controller;

import com.cvizard.pdfcreator.model.Resume;
import com.cvizard.pdfcreator.repository.ResumeRepository;
import com.cvizard.pdfcreator.service.ResumeService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/maker")
@AllArgsConstructor
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final ResumeService resumeService;

    @GetMapping(path = "/download",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getPdfFile(@RequestParam(name = "key") String key) throws IOException, DocumentException {
        Resume resume = resumeRepository.findById(key)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.FORBIDDEN,"CV is processing"));

        resumeService.createPdf(key, resume);
        File file = new File("resources/" + key + ".pdf");
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok().body(resource);

    }
}
