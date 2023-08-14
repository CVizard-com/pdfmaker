package com.cvizard.pdfcreator.service;

import com.cvizard.pdfcreator.model.Resume;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
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

    public void createPdf(String key, Resume resume) throws IOException, DocumentException {

        context.setVariable("resume",resume);
        String processed = templateEngine.process("resume", context);
        renderer.setDocumentFromString(processed);
        renderer.setPDFPageSize(new Rectangle(842, 595));
        renderer.layout();

        try (FileOutputStream fos = new FileOutputStream("resources/"+key+".pdf")) {
            renderer.createPDF(fos);
        }
    }

}
