package org.backend.project.reportingapp.service.Abstract;

import org.backend.project.reportingapp.dto.request.LaborantDto;
import org.backend.project.reportingapp.dto.response.LaborantResponse;

import java.util.List;

public interface LaborantService {
    LaborantResponse createLaborant(LaborantDto laborantDto);

    List<LaborantResponse> getAllLaborants(int page, int size, String sortBy, String sortOrder, String filter);

    LaborantResponse updateLaborant(Long id, LaborantDto laborantDto);

    void deleteLaborant(Long id);
}
