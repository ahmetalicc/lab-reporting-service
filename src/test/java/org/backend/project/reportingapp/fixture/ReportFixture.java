package org.backend.project.reportingapp.fixture;

import org.backend.project.reportingapp.dto.request.ReportDto;
import org.backend.project.reportingapp.entity.Report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportFixture extends Fixture<Report>{

    LaborantFixture laborantFixture = new LaborantFixture();
    public Report createReport() {
        Report report = new Report();
        report.setId(faker.number().randomNumber());
        report.setFileNumber(UUID.randomUUID().toString());
        report.setPatientName(faker.name().firstName());
        report.setPatientSurname(faker.name().lastName());
        report.setPatientIdentityNumber(faker.number().digits(11));
        report.setDiagnosisTitle(faker.lorem().sentence(3));
        report.setDiagnosisDetails(faker.lorem().paragraph(3));
        report.setReportDate(LocalDate.now());
        report.setReportImage(faker.avatar().image().getBytes());
        report.setLaborant(laborantFixture.createLaborant());
        return report;
    }

    public List<Report> createReportList(){
        List<Report> reportList = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            Report report = new Report();
            report.setId(faker.number().randomNumber());
            report.setFileNumber(UUID.randomUUID().toString());
            report.setPatientName(faker.name().firstName());
            report.setPatientSurname(faker.name().lastName());
            report.setPatientIdentityNumber(faker.number().digits(11));
            report.setDiagnosisTitle(faker.lorem().sentence(3));
            report.setDiagnosisDetails(faker.lorem().paragraph(3));
            report.setReportDate(LocalDate.now());
            report.setReportImage(faker.avatar().image().getBytes());
            report.setLaborant(laborantFixture.createLaborant());

            reportList.add(report);
        }
        return reportList;
    }

    public ReportDto createReportDto(){
        ReportDto reportDto = new ReportDto();
        reportDto.setFileNumber(UUID.randomUUID().toString());
        reportDto.setPatientName(faker.name().firstName());
        reportDto.setPatientSurname(faker.name().lastName());
        reportDto.setPatientIdentityNumber(faker.number().digits(11));
        reportDto.setDiagnosisTitle(faker.lorem().sentence(3));
        reportDto.setDiagnosisDetails(faker.lorem().paragraph(3));
        reportDto.setReportDate(LocalDate.now());
        reportDto.setReportImage(faker.gameOfThrones().character());
        reportDto.setLaborantId(faker.number().randomNumber());

        return reportDto;
    }
}
