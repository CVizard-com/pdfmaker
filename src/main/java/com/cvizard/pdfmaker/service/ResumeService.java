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
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.io.FileOutputStream;
import java.net.URISyntaxException;

@Service
@AllArgsConstructor
public class ResumeService {

    private final SpringTemplateEngine templateEngine;
    private final ITextRenderer renderer = new ITextRenderer();
    private final Context context = new Context();

    public void createPdf(String key, Resume resume) throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream("resources/"+key+"-logo.pdf");
        String processed = templateEngine.process("resume", context);
        Image img = new Image(ImageDataFactory.create("/app/resources/logo.png"));
        System.out.println("createPdf " + resume);
        context.setVariable("resume",resume);
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
