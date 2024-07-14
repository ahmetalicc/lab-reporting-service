package org.backend.project.reportingapp.core.responses;

import lombok.Getter;

@Getter
public class Response {
    private boolean success;
    private String message;

    public Response(boolean success){this.success = success;}

    public Response(boolean success, String message){
        this(success);
        this.message = message;
    }

}
