package com.cvizard.pdfcreator.service;

import com.cvizard.pdfcreator.model.Resume;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.URL;

@Service
@AllArgsConstructor
public class ResumeService {

    private final SpringTemplateEngine templateEngine;
    private final ITextRenderer renderer = new ITextRenderer();
    private final Context context = new Context();

    public void createPdf(String key, Resume resume) throws IOException, DocumentException, URISyntaxException {

        context.setVariable("resume",resume);
        String processed = templateEngine.process("resume", context);
        renderer.setDocumentFromString(processed);
        renderer.layout();

        try (FileOutputStream fos = new FileOutputStream("resources/"+key+"-logo.pdf")) {
            renderer.createPDF(fos);
        }
        String source = "resources/"+key+"-logo.pdf";
        String destination = "resources/"+key+".pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(source), new PdfWriter(destination));
        Document document = new Document(pdfDoc);

        int numberOfPages = pdfDoc.getNumberOfPages();

        URL resource = getClass().getClassLoader().getResource("static/logo.png");
        File file = new File(resource.toURI());
        Image img = new Image(ImageDataFactory.create(file.getAbsolutePath()));
        for (int i = 1; i <= numberOfPages; i++) {
            img.setFixedPosition(i, 420, 735);
            document.add(img);
        }

        document.close();
    }

}
