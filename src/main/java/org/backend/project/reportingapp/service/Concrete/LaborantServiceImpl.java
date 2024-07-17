package org.backend.project.reportingapp.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.dao.LaborantRepository;
import org.backend.project.reportingapp.dto.request.LaborantDto;
import org.backend.project.reportingapp.dto.response.LaborantResponse;
import org.backend.project.reportingapp.entity.Laborant;
import org.backend.project.reportingapp.service.Abstract.LaborantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaborantServiceImpl implements LaborantService {

    private final LaborantRepository laborantRepository;
    @Override
    public LaborantResponse createLaborant(LaborantDto laborantDto) {
        if (laborantDto.getFirstName() == null || laborantDto.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("Laborant's first name cannot be null or empty.");
        }
        if (laborantDto.getLastName() == null || laborantDto.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Laborant's last name cannot be null or empty.");
        }
        if (laborantDto.getHospitalId() == null || laborantDto.getHospitalId().isEmpty()) {
            throw new IllegalArgumentException("Laborant's hospital identity id cannot be null or empty.");
        }

        // Check if a laborant with the same hospital ID already exists
        Optional<Laborant> existingLaborant = laborantRepository.findByHospitalId(laborantDto.getHospitalId());
        if (existingLaborant.isPresent()) {
            throw new IllegalArgumentException("A laborant with this hospital ID already exists.");
        }
        Laborant laborant = new Laborant();
        laborant.setFirstName(laborantDto.getFirstName());
        laborant.setLastName(laborantDto.getLastName());
        laborant.setHospitalId(laborantDto.getHospitalId());

        laborantRepository.save(laborant);

        return LaborantResponse.Convert(laborant);
    }

    @Override
    public List<LaborantResponse> getAllLaborants(int page, int size, String sortBy, String sortOrder, String filter) {
        Sort sort = Sort.by(sortBy).ascending();
        if ("desc".equals(sortOrder)) {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Laborant> laborants;
        if (filter == null || filter.trim().isEmpty()) {
            laborants = laborantRepository.findAll(pageable);
        } else {
            laborants = laborantRepository.findLaborantsByFilter(filter.toLowerCase(), pageable);
        }

        return laborants.stream()
                .map(LaborantResponse::Convert)
                .collect(Collectors.toList());

    }

    @Override
    public LaborantResponse updateLaborant(Long id, LaborantDto laborantDto) {
        Laborant laborant = laborantRepository.findById(id).orElseThrow(
                ()-> new NullPointerException(String.format("Laborant not found with id: %s", id)));

        boolean isUpdated = false;

        if (!laborant.getFirstName().equals(laborantDto.getFirstName())) {
            laborant.setFirstName(laborantDto.getFirstName());
            isUpdated = true;
        }
        if (!laborant.getLastName().equals(laborantDto.getLastName())) {
            laborant.setLastName(laborantDto.getLastName());
            isUpdated = true;
        }
        if (!laborant.getHospitalId().equals(laborantDto.getHospitalId())) {
            laborant.setHospitalId(laborantDto.getHospitalId());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("No fields have been updated. Please provide different values.");
        }
        laborantRepository.save(laborant);

        return LaborantResponse.Convert(laborant);
    }

    @Override
    public void deleteLaborant(Long id) {
        Laborant laborant = laborantRepository.findById(id).orElseThrow(
                ()-> new NullPointerException(String.format("Laborant not found with id: %s", id)));
        laborantRepository.delete(laborant);
    }
}