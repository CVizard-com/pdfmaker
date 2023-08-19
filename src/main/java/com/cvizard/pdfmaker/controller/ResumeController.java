package com.cvizard.pdfmaker.controller;

import com.cvizard.pdfmaker.model.Resume;
import com.cvizard.pdfmaker.repository.ResumeRepository;
import com.cvizard.pdfmaker.service.ResumeService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

import static com.cvizard.pdfmaker.model.ResumeStatus.ERROR;

@RestController
@RequestMapping("/api/maker")
@AllArgsConstructor
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final ResumeService resumeService;

    @GetMapping(path = "/download",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getPdfFile(@RequestParam(name = "key") String key) throws IOException, DocumentException {
        ResponseEntity <?> responseEntity;
        Resume resume = resumeRepository.findById(key).orElse(Resume.builder().status(ERROR).build());

        switch (resume.getStatus()) {
            case READY -> {
                System.out.println("READY");
                resumeService.createPdf(key, resume);
                File file = new File("resources/" + key + ".pdf");
                Resource resource = new FileSystemResource(file);
                responseEntity = ResponseEntity.status(200).body(resource);
                file.delete();
                resumeRepository.delete(resume);
            }
            case PROCESSING -> {System.out.println("PROCESSING");
                responseEntity = ResponseEntity.status(202).body(null);}
            case ERROR -> {
                System.out.println("ERROR"); responseEntity = ResponseEntity.status(404).body(null);}
            default -> responseEntity = ResponseEntity.status(422).body(null);
        }
        return responseEntity;
    }
}