package com.cvizard.pdfmaker.service;

import com.cvizard.pdfmaker.client.GotenbergClient;
import com.cvizard.pdfmaker.exceptions.NoResumeException;
import com.cvizard.pdfmaker.exceptions.StillProcessingException;
import com.cvizard.pdfmaker.model.Resume;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.mock.web.MockMultipartFile;

import java.io.*;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final SpringTemplateEngine templateEngine;
    private final Context context = new Context();

    private final GotenbergClient gotenbergClient;

    public ResponseEntity<?> createResponse(Resume resume, String key, String template, String fileFormat) throws DocumentException, IOException {
        ResponseEntity<?> responseEntity;
        switch (resume.getStatus()) {
            case READY -> {
                log.info("Resume is ready " + resume.getId());
                createPdf(key, resume, template);
                if (fileFormat.equals("docx")) {
                    createDocx(key);
                }
                responseEntity = getResponseEntity(key, fileFormat);
            }
            case PROCESSING -> {
                throw new StillProcessingException();
            }
            default -> throw new NoResumeException();
        }
        return responseEntity;
    }

    private ResponseEntity<?> getResponseEntity(String key, String fileFormat) {

        File file = new File("resources/" + key + "." + fileFormat);
        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();

        if (fileFormat.equals("pdf")) {

            headers.setContentType(MediaType.APPLICATION_PDF);

        } else if (fileFormat.equals("docx")) {

            headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        }
        return ResponseEntity
                .status(200)
                .headers(headers)
                .body(resource);
    }

    public void createPdf(String key, Resume resume, String template) throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream("resources/"+key+"-logo.pdf");

        context.setVariable("resume",resume);
        String processed = templateEngine.process("resume"+template, context);

        String tempDir = System.getProperty("java.io.tmpdir");
        String fileName = "index.html";
        Path tempPath = Paths.get(tempDir, fileName);
        Files.writeString(tempPath, processed);

        MultipartFile htmlFile = new MockMultipartFile(
                "index.html",
                "index.html",
                (String)null,
                new FileInputStream(tempPath.toFile())
        );

        ResponseEntity<byte[]> response = gotenbergClient.convertHtml(htmlFile);
        fos.write(Objects.requireNonNull(response.getBody()));
        fos.close();

        Files.delete(tempPath);
        addLogoToPdf(key);

    }
    public void addLogoToPdf(String key) throws IOException {

        Image img = new Image(ImageDataFactory.create("/app/resources/logo.png"));
        PdfDocument pdfDoc =
                new PdfDocument(
                        new PdfReader("resources/"+key+"-logo.pdf"),
                        new PdfWriter("resources/"+key+".pdf")
                );
        Document document = new Document(pdfDoc);

        int numberOfPages = pdfDoc.getNumberOfPages();

        for (int i = 1; i <= numberOfPages; i++) {
            img.setFixedPosition(i, 420, 700);
            document.add(img);
        }
        new File("resources/"+key+"-logo.pdf").delete();
        document.close();
    }
    public void createDocx(String key) {
        com.spire.pdf.PdfDocument pdf = new  com.spire.pdf.PdfDocument();
        pdf.loadFromFile("resources/"+key+".pdf");
        pdf.getConvertOptions().setConvertToWordUsingFlow(true);
        pdf.saveToFile("resources/"+key+".docx", com.spire.pdf.FileFormat.DOCX);
        pdf.close();
    }
}
