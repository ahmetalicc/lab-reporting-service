package org.backend.project.reportingapp.core.responses;

public class SuccessResponse extends Response{

    public SuccessResponse(){
        super(true);
    }

    public SuccessResponse(String message){
        super(true, message);
    }
}
