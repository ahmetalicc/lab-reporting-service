package org.backend.project.reportingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.project.reportingapp.entity.Report;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponse {
    private String fileNumber;
    private String patientName;
    private String patientSurname;
    private String patientIdentityNumber;
    private String diagnosisTitle;
    private String diagnosisDetails;
    private LocalDate reportDate;
    private byte[]reportImage;
    private Long laborantId;

    public static ReportResponse Convert(Report report){
        return ReportResponse.builder()
                .fileNumber(report.getFileNumber())
                .patientName(report.getPatientName())
                .patientSurname(report.getPatientSurname())
                .patientIdentityNumber(report.getPatientIdentityNumber())
                .diagnosisTitle(report.getDiagnosisTitle())
                .diagnosisDetails(report.getDiagnosisDetails())
                .reportDate(report.getReportDate())
                .reportImage(report.getReportImage())
                .laborantId(report.getLaborant().getId())
                .build();
    }
}
