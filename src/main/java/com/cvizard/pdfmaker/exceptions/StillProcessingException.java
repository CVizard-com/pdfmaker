package com.cvizard.pdfmaker.exceptions;

public class StillProcessingException extends RuntimeException {
    public StillProcessingException() {
        super("Resume is still processing");
    }
}
