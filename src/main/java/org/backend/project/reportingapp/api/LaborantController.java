package org.backend.project.reportingapp.api;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.core.responses.*;
import org.backend.project.reportingapp.dto.request.LaborantDto;
import org.backend.project.reportingapp.dto.response.LaborantResponse;
import org.backend.project.reportingapp.service.Abstract.LaborantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("laborant")
public class LaborantController {

    private final LaborantService laborantService;
    @PostMapping("/createLaborant")
    public ResponseEntity<DataResponse<LaborantResponse>> createLaborant(@RequestBody @Valid LaborantDto laborantDto){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDataResponse<>(laborantService.createLaborant(laborantDto), "Laborant created successfully."));
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }

    @GetMapping("/getAllLaborants")
    public ResponseEntity<DataResponse<List<LaborantResponse>>> getAllLaborants(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "3") int size,
                                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                                @RequestParam(defaultValue = "asc") String sortOrder,
                                                                                @RequestParam(required = false) String filter){
        return ResponseEntity.ok(new SuccessDataResponse<>(laborantService.getAllLaborants(page, size, sortBy, sortOrder, filter), "Data has been listed successfully."));
    }

    @PutMapping("/updateLaborant/{id}")
    public ResponseEntity<DataResponse<LaborantResponse>> updateLaborant(@PathVariable("id") Long id, @RequestBody LaborantDto laborantDto){
        try{
            return ResponseEntity.status(HttpStatus.OK).body
                    (new SuccessDataResponse<>(laborantService.updateLaborant(id, laborantDto), "Laborant is updated successfully."));
        }
        catch (NullPointerException | IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }

    @DeleteMapping("/deleteLaborant/{id}")
    public ResponseEntity<Response> deleteLaborant(@PathVariable("id") Long id){
        try{
            laborantService.deleteLaborant(id);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Laborant is deleted successfully."));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }
}
