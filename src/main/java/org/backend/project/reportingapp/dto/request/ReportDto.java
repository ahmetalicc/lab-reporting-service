package org.backend.project.reportingapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {
    private String fileNumber;
    private String patientName;
    private String patientSurname;
    @Size(min = 11, max = 11, message = "Field length must be exactly 11 characters")
    private String patientIdentityNumber;
    private String diagnosisTitle;
    private String diagnosisDetails;
    private LocalDate reportDate;
    private String reportImage;
    private Long laborantId;
}
