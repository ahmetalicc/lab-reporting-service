package org.backend.project.reportingapp.core.responses;

public class SuccessDataResponse<T> extends DataResponse<T> {
    public SuccessDataResponse(T data, String message) {
        super(data, true ,message);
    }
    public SuccessDataResponse(T data) {
        super(data,true);
    }

    public SuccessDataResponse(String message) {
        super(null, true ,message);
    }

    public SuccessDataResponse() {
        super(null, true);
    }
}
