package org.backend.project.reportingapp.api;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.core.responses.*;
import org.backend.project.reportingapp.dto.request.ReportDto;
import org.backend.project.reportingapp.dto.response.ReportResponse;
import org.backend.project.reportingapp.service.Abstract.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("report")
public class ReportController {

    private final ReportService reportService;
    @GetMapping("/getAllReports")
    public ResponseEntity<DataResponse<List<ReportResponse>>> getAllReports(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "3") int size,
                                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                                           @RequestParam(defaultValue = "asc") String sortOrder,
                                                                           @RequestParam(required = false) String patientName,
                                                                           @RequestParam(required = false) String patientSurname,
                                                                           @RequestParam(required = false) String patientIdentityNumber,
                                                                           @RequestParam(required = false) String laborantName,
                                                                           @RequestParam(required = false) String laborantSurname){
            return ResponseEntity.ok(new SuccessDataResponse<>(reportService.getAllReports(page, size, sortBy, sortOrder, patientName, patientSurname, patientIdentityNumber, laborantName, laborantSurname), "Data has been listed successfully."));

    }

    @PostMapping("/generateReport")
    public ResponseEntity<DataResponse<ReportResponse>> generateReport(@RequestBody ReportDto reportDto){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDataResponse<>(reportService.generateReport(reportDto), "Report generated successfully."));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }

    @GetMapping("/getOneReport/{id}")
    public ResponseEntity<DataResponse<ReportResponse>> getOneReport(@PathVariable("id")  Long id ){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(reportService.getOneReport(id),"Data has been listed successfully."));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }

    @PutMapping("/updateReport/{id}")
    public ResponseEntity<DataResponse<ReportResponse>> updateReport(@PathVariable("id") Long id, @RequestBody ReportDto reportDto){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(reportService.updateReport(id, reportDto), "Report updated successfully."));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }

    @DeleteMapping("/deleteReport/{id}")
    public ResponseEntity<Response> deleteReport(@PathVariable("id") Long id){
        try{
            reportService.deleteReport(id);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Report deleted successfully."));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }
}
