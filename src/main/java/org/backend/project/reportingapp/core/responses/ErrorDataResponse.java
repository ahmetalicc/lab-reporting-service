package org.backend.project.reportingapp.core.responses;

public class ErrorDataResponse<T> extends DataResponse<T>{
    public ErrorDataResponse(T data, String message) {
        super(data, false ,message);
    }

    public ErrorDataResponse(T data) {
        super(data,false);
    }

    public ErrorDataResponse(String message) {
        super(null, false ,message);
    }

    public ErrorDataResponse() {
        super(null, false);
    }
}
