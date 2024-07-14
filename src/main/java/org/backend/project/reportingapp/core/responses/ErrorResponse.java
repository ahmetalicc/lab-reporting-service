package org.backend.project.reportingapp.core.responses;


public class ErrorResponse extends Response{
    public ErrorResponse() {
        super(false);
    }

    public ErrorResponse(String message) {
        super(false, message);
    }
}
