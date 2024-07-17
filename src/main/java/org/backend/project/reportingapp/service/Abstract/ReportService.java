package org.backend.project.reportingapp.service.Abstract;

import org.backend.project.reportingapp.dto.request.ReportDto;
import org.backend.project.reportingapp.dto.response.ReportResponse;

import java.util.List;

public interface ReportService {
    List<ReportResponse> getAllReports(int page, int size, String sortBy, String sortOrder, String patientName, String patientSurname, String patientIdentityNumber, String laborantName, String laborantSurname);

    ReportResponse generateReport(ReportDto reportDto);

    ReportResponse getOneReport(Long id);

    ReportResponse updateReport(Long id, ReportDto reportDto);

    void deleteReport(Long id);
}
