package org.backend.project.reportingapp.service.concrete;

import org.backend.project.reportingapp.dao.LaborantRepository;
import org.backend.project.reportingapp.dao.ReportRepository;
import org.backend.project.reportingapp.dto.request.ReportDto;
import org.backend.project.reportingapp.dto.response.ReportResponse;
import org.backend.project.reportingapp.entity.Report;
import org.backend.project.reportingapp.fixture.ReportFixture;
import org.backend.project.reportingapp.service.Concrete.ReportServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private LaborantRepository laborantRepository;
    @Captor
    private ArgumentCaptor<Report> reportCaptor;
    @InjectMocks
    private ReportServiceImpl reportService;

    @DisplayName("The test that when called with page size, sortBy, sortOrder, and filter should return a list of ReportResponse with pagination")
    @ParameterizedTest
    @ValueSource(strings = {"Jane", "Doe", "12345678901", "John"})
    void getAllReports_whenCalledWithFilters_ShouldReturnReportResponseListWithPagination(String filter) {
        int page = 0;
        int size = 10;
        String sortBy = "reportDate";
        String sortOrder = "asc";
        ReportFixture reportFixture = new ReportFixture();
        List<Report> report = reportFixture.createReportList();
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Report> pagedReports = new PageImpl<>(report);

        when(reportRepository.findAllByFilters(anyString(), anyString(), anyString(), anyString(), anyString(), eq(pageable))).thenReturn(pagedReports);

        List<ReportResponse> actual = reportService.getAllReports(page, size, sortBy, sortOrder, filter, filter, filter, filter, filter);
        List<ReportResponse> expected = pagedReports.stream()
                .map(ReportResponse::Convert)
                        .toList();

        assertEquals(actual, expected);
        assertThat(filter).isNotNull();

        verify(reportRepository, times(1)).findAllByFilters(anyString(), anyString(), anyString(), anyString(), anyString(), eq(pageable));
    }
    @DisplayName("The test that when call with page size sortBy sortOrder parameters should return list of ReportResponse list with pagination")
    @Test
    void getAllReports_whenCallWithoutFilters_shouldReturnReportResponseListWithPagination() {
        int page = 0;
        int size = 10;
        String sortBy = "reportDate";
        String sortOrder = "asc";

        ReportFixture reportFixture = new ReportFixture();
        List<Report> report = reportFixture.createReportList();
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Report> pagedReports = new PageImpl<>(report);

        when(reportRepository.findAllByFilters(null, null, null, null, null, pageable)).thenReturn(pagedReports);

        List<ReportResponse> actual = reportService.getAllReports(page, size, sortBy, sortOrder, null, null, null, null, null);
        List<ReportResponse> expected = pagedReports.stream()
                        .map(ReportResponse::Convert)
                                .toList();

        assertEquals(actual, expected);

        verify(reportRepository, times(1)).findAllByFilters(null, null, null, null, null, pageable);
    }
    @DisplayName("The test that when call with page size sortBy sortOrder parameters should sorts Reports in descending order when sort order is descending")
    @Test
    void getAllReports_whenCallWithoutFilters_ShouldSortsReportsDescending_whenSortOrderIsDescending() {
        int page = 0;
        int size = 10;
        String sortBy = "reportDate";
        String sortOrder = "desc";

        ReportFixture reportFixture = new ReportFixture();
        List<Report> report = reportFixture.createReportList();
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Report> pagedReports = new PageImpl<>(report);

        when(reportRepository.findAllByFilters(null, null, null, null, null, pageable)).thenReturn(pagedReports);

        List<ReportResponse> actual = reportService.getAllReports(page, size, sortBy, sortOrder, null, null, null, null, null);
        List<ReportResponse> expected = pagedReports.stream()
                .map(ReportResponse::Convert)
                .toList();

        assertEquals(actual, expected);

        verify(reportRepository, times(1)).findAllByFilters(null, null, null, null, null, pageable);
    }
    @DisplayName("The test when call with valid ReportDto information should generate report")
    @Test
    void generateReport_whenCallWithValidReportDtoInformation_ShouldGenerateReport(){
        ReportFixture reportFixture = new ReportFixture();
        ReportDto reportDto = reportFixture.createReportDto();
        Report report = reportFixture.createReport();

        when(laborantRepository.findById(reportDto.getLaborantId())).thenReturn(Optional.of(report.getLaborant()));

        ReportResponse response = reportService.generateReport(reportDto);

        verify(reportRepository).save(reportCaptor.capture());
        Report savedReport = reportCaptor.getValue();
        assertThat(savedReport.getPatientName()).isEqualTo(reportDto.getPatientName());
        assertThat(savedReport.getPatientSurname()).isEqualTo(reportDto.getPatientSurname());
        assertThat(savedReport.getPatientIdentityNumber()).isEqualTo(reportDto.getPatientIdentityNumber());
        assertThat(savedReport.getFileNumber()).isEqualTo(reportDto.getFileNumber());
        assertThat(response).isEqualTo(ReportResponse.Convert(savedReport));

        verify(laborantRepository, times(1)).findById(reportDto.getLaborantId());
        verify(reportRepository, times(1)).save(any(Report.class));
    }
    @DisplayName("The test when laborant not found in database should throw NullPointerException")
    @Test
    void generateReport_whenLaborantNotFound_ShouldThrowNullPointerException(){
        ReportFixture reportFixture = new ReportFixture();
        ReportDto reportDto = reportFixture.createReportDto();
        when(laborantRepository.findById(reportDto.getLaborantId())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> reportService.generateReport(reportDto))
                .isInstanceOf(NullPointerException.class)
                        .hasMessageContaining(String.format("Laborant not found with id: %s", reportDto.getLaborantId()));
        verify(laborantRepository).findById(reportDto.getLaborantId());
        verify(reportRepository, never()).save(any(Report.class));
    }
    @DisplayName("The test when call with id parameter that should return ReportResponse if Report exist with given id")
    @Test
    void getOneProduct_whenCallWithIdParameter_ShouldReturnReportResponse_IfReportExistWithGivenId(){
        ReportFixture reportFixture = new ReportFixture();
        Report report = reportFixture.createReport();
        Long id = report.getId();

        when(reportRepository.findById(id)).thenReturn(Optional.of(report));

        ReportResponse actual = reportService.getOneReport(id);
        ReportResponse expected = ReportResponse.Convert(report);

        assertEquals(actual, expected);
        assertNotNull(actual);
        verify(reportRepository, times(1)).findById(id);
    }
    @DisplayName("The test when call with id parameter that should throw NullPointerException if Report is not exist with given id")
    @Test
    void getOneProduct_whenCallWithIdParameter_ShouldThrowNullPointerException_IfReportIsNotExistWithGivenId(){
        ReportFixture reportFixture = new ReportFixture();
        Report report = reportFixture.createReport();
        Long id = report.getId();

        when(reportRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> reportService.getOneReport(id))
                .isInstanceOf(NullPointerException.class)
                        .hasMessageContaining(String.format("Report not found with given id: %s", id));
        verify(reportRepository, times(1)).findById(id);
    }
    @DisplayName("The test when call with valid id and ReportDto parameters should return ReportResponse")
    @Test
    void updateReport_whenCallWithValidIdAndReportDtoParameters_ShouldReturnReportResponse(){
        ReportFixture reportFixture = new ReportFixture();
        Report report = reportFixture.createReport();
        ReportDto reportDto = reportFixture.createReportDto();

        when(laborantRepository.findById(reportDto.getLaborantId())).thenReturn(Optional.of(report.getLaborant()));
        when(reportRepository.findById(report.getId())).thenReturn(Optional.of(report));
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        ReportResponse response = reportService.updateReport(report.getId(), reportDto);

        assertThat(response).isEqualTo(ReportResponse.Convert(report));
        verify(laborantRepository,times(1)).findById(reportDto.getLaborantId());
        verify(reportRepository,times(1)).findById(report.getId());
        verify(reportRepository,times(1)).save(any(Report.class));
    }
    @DisplayName("The test when laborant not found should throw NullPointerException")
    @Test
    void updateReport_whenLaborantNotFound_ShouldThrowNullPointerException(){
        ReportFixture reportFixture = new ReportFixture();
        Report report = reportFixture.createReport();
        ReportDto reportDto = reportFixture.createReportDto();

        when(laborantRepository.findById(reportDto.getLaborantId())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> reportService.updateReport(report.getId(), reportDto))
                .isInstanceOf(NullPointerException.class)
                        .hasMessageContaining(String.format("Laborant not found with id: %s", reportDto.getLaborantId()));
        verify(laborantRepository).findById(reportDto.getLaborantId());
        verify(reportRepository, never()).findById(report.getId());
        verify(reportRepository, never()).save(any(Report.class));
    }
    @DisplayName("The test when report not found with given id should throw NullPointerException")
    @Test
    void updateReport_whenReportNotFoundWithGivenId_ShouldThrowNullPointerException(){
        ReportFixture reportFixture = new ReportFixture();
        Report report = reportFixture.createReport();
        ReportDto reportDto = reportFixture.createReportDto();

        when(laborantRepository.findById(reportDto.getLaborantId())).thenReturn(Optional.of(report.getLaborant()));
        when(reportRepository.findById(report.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> reportService.updateReport(report.getId(), reportDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(String.format("Report not found with given id: %s", report.getId()));
        verify(laborantRepository, times(1)).findById(reportDto.getLaborantId());
        verify(reportRepository, times(1)).findById(report.getId());
        verify(reportRepository, never()).save(any(Report.class));
    }
    @DisplayName("The test when call with valid id parameter should delete report from database")
    @Test
    void deleteReport_whenCallWithValidId_ShouldDeleteReportFromDB(){
        ReportFixture reportFixture = new ReportFixture();
        Report report = reportFixture.createReport();

        when(reportRepository.findById(report.getId())).thenReturn(Optional.of(report));

        reportService.deleteReport(report.getId());

        verify(reportRepository, times(1)).findById(report.getId());
        verify(reportRepository, times(1)).delete(report);
    }
    @DisplayName("The test when call with non existing id parameter should throw NullPointerException")
    @Test
    void deleteReport_whenCallWithNonExistingId_ShouldThrowNullPointerException(){
        ReportFixture reportFixture = new ReportFixture();
        Report report = reportFixture.createReport();
        when(reportRepository.findById(report.getId())).thenReturn(Optional.empty());


        assertThatThrownBy(()-> reportService.deleteReport(report.getId()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(String.format("Report not found with given id: %s", report.getId()));
    }
}
