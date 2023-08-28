package com.cvizard.pdfmaker.exceptions;

public class BadFormatResumeException extends RuntimeException {
    public BadFormatResumeException() {
        super("Resume is not in the correct format");
    }
}
