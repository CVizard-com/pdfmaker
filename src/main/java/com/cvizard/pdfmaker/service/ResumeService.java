package com.cvizard.pdfmaker.service;

import com.cvizard.pdfmaker.model.Resume;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.io.FileOutputStream;

@Service
@AllArgsConstructor
public class ResumeService {

    private final SpringTemplateEngine templateEngine;
    private final ITextRenderer renderer = new ITextRenderer();
    private final Context context = new Context();

    public ResponseEntity<?> createResponse(Resume resume, String key, String template) throws DocumentException, IOException {
        ResponseEntity<?> responseEntity;
        switch (resume.getStatus()) {
            case READY: {
                createPdf(key, resume, template);
                File file = new File("resources/" + key + ".pdf");
                Resource resource = new FileSystemResource(file);
                responseEntity = ResponseEntity.status(200).body(resource);
//                file.delete();
//                resumeRepository.delete(resume);
                break;
            }
            case PROCESSING: {
                responseEntity = ResponseEntity.status(425).body(null);
                break;
            }
            case ERROR: {
                responseEntity = ResponseEntity.status(404).body(null);
                break;
            }
            default: {
                responseEntity = ResponseEntity.status(422).body(null);
                break;
            }
        }
        return responseEntity;
    }

    public void createPdf(String key, Resume resume, String template) throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream("resources/"+key+"-logo.pdf");
        Image img = new Image(ImageDataFactory.create("/app/resources/logo.png"));

        context.setVariable("resume",resume);
        String processed = templateEngine.process("resume"+template, context);
        renderer.setDocumentFromString(processed);
        renderer.layout();
        renderer.createPDF(fos);

        PdfDocument pdfDoc =
                new PdfDocument(
                        new PdfReader("resources/"+key+"-logo.pdf"),
                        new PdfWriter("resources/"+key+".pdf")
                );
        Document document = new Document(pdfDoc);

        int numberOfPages = pdfDoc.getNumberOfPages();

        for (int i = 1; i <= numberOfPages; i++) {
            img.setFixedPosition(i, 420, 735);
            document.add(img);
        }
        new File("resources/"+key+"-logo.pdf").delete();
        document.close();
    }
}
