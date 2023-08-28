package com.cvizard.pdfmaker.exceptions;


public class NoResumeException extends RuntimeException{
    public NoResumeException() {
        super("Resume could not be found");
    }

}
