package com.cvizard.pdfmaker.exceptions;

import com.itextpdf.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    ResponseEntity<String> handleIOException(Exception ex){
        log.error("io exception has been thrown");
        log.error(ex.getMessage());
        String output =
                "this error has been found" +
                ex.getMessage();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(output);
    }

    @ExceptionHandler(DocumentException.class)
    ResponseEntity<String> handleDocumentException(Exception ex){
        log.error("document exception has been thrown");
        log.error(ex.getMessage());
        String output =
                "this error has been found" +
                        ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(output);
    }

    @ExceptionHandler(NoResumeException.class)
    ResponseEntity<String> handleResumeException(NoResumeException noResumeException){
        log.error("resume exception has been thrown");
        log.error(noResumeException.getMessage());
        String output =
                "this error has been found -> " +
                        noResumeException.getMessage();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(output);
    }

    @ExceptionHandler(StillProcessingException.class)
    ResponseEntity<String> handleStillProcessingException(StillProcessingException stillProcessingException){
        log.error("resume exception has been thrown");
        log.error(stillProcessingException.getMessage());
        String output =
                "this error has been found -> " +
                        stillProcessingException.getMessage();
        return ResponseEntity
                .status(HttpStatus.PROCESSING)
                .body(output);
    }

}
