package org.backend.project.reportingapp.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.dao.LaborantRepository;
import org.backend.project.reportingapp.dao.ReportRepository;
import org.backend.project.reportingapp.dto.request.ReportDto;
import org.backend.project.reportingapp.dto.response.ReportResponse;
import org.backend.project.reportingapp.entity.Laborant;
import org.backend.project.reportingapp.entity.Report;
import org.backend.project.reportingapp.service.Abstract.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    private final LaborantRepository laborantRepository;
    @Override
    public List<ReportResponse> getAllReports(int page, int size, String sortBy, String sortOrder, String patientName, String patientSurname, String patientIdentityNumber, String laborantName, String laborantSurname) {
        Sort sort = Sort.by(sortBy).ascending();
        if ("desc".equals(sortOrder)) {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Report> reports;
        reports = reportRepository.findAllByFilters(patientName, patientSurname, patientIdentityNumber, laborantName, laborantSurname, pageable);
        return reports.stream()
                .map(ReportResponse::Convert)
                .collect(Collectors.toList());
    }

    @Override
    public ReportResponse generateReport(ReportDto reportDto) {
        Laborant laborant = laborantRepository.findById(reportDto.getLaborantId()).orElseThrow(
                ()-> new NullPointerException(String.format("Laborant not found with id: %s", reportDto.getLaborantId())));

        Report report = new Report();
        return getReportResponse(reportDto, laborant, report);
    }

    @Override
    public ReportResponse getOneReport(Long id) {
        Report report = reportRepository.findById(id).orElseThrow(
                ()-> new NullPointerException(String.format("Report not found with given id: %s", id)));

        return ReportResponse.Convert(report);
    }

    @Override
    public ReportResponse updateReport(Long id, ReportDto reportDto) {
        Laborant laborant = laborantRepository.findById(reportDto.getLaborantId()).orElseThrow(
                ()-> new NullPointerException(String.format("Laborant not found with id: %s", reportDto.getLaborantId())));

        Report report = reportRepository.findById(id).orElseThrow(
                ()-> new NullPointerException(String.format("Report not found with given id: %s", id)));

        return getReportResponse(reportDto, laborant, report);
    }

    @Override
    public void deleteReport(Long id) {
        Report report = reportRepository.findById(id).orElseThrow(
                ()-> new NullPointerException(String.format("Report not found with given id: %s", id)));
        reportRepository.delete(report);
    }

    private ReportResponse getReportResponse(ReportDto reportDto, Laborant laborant, Report report) {
        report.setFileNumber(reportDto.getFileNumber());
        report.setPatientName(reportDto.getPatientName());
        report.setPatientSurname(reportDto.getPatientSurname());
        report.setPatientIdentityNumber(reportDto.getPatientIdentityNumber());
        report.setDiagnosisTitle(reportDto.getDiagnosisTitle());
        report.setDiagnosisDetails(reportDto.getDiagnosisDetails());
        report.setReportDate(reportDto.getReportDate());
        report.setReportImage(reportDto.getReportImage().getBytes());
        report.setLaborant(laborant);

        reportRepository.save(report);

        return ReportResponse.Convert(report);
    }
}
