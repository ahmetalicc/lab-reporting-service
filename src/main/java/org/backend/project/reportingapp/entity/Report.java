package org.backend.project.reportingapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fileNumber", length = 50, nullable = false, unique = true)
    private String fileNumber;
    @Column(name = "patientName", length = 50, nullable = false)
    private String patientName;
    @Column(name = "patientSurname", length = 50, nullable = false)
    private String patientSurname;
    @Column(name = "patientIdentityNumber", length = 11, nullable = false)
    @Size(min = 11, max = 11, message = "Field length must be exactly 11 characters")
    private String patientIdentityNumber;
    @Column(name = "diagnosisTitle", length = 100, nullable = false)
    private String diagnosisTitle;
    @Column(name = "diagnosisDetails", length = 1000, nullable = false)
    private String diagnosisDetails;
    @Column(name = "reportDate", length = 50, nullable = false)
    private LocalDate reportDate;
    @Column(name = "reportImage")
    private byte[]reportImage;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "laborant_id")
    private Laborant laborant;
}