package org.backend.project.reportingapp.service.Concrete;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.backend.project.reportingapp.dao.LaborantRepository;
import org.backend.project.reportingapp.dao.RoleRepository;
import org.backend.project.reportingapp.dao.UserRepository;
import org.backend.project.reportingapp.dto.request.LaborantDto;
import org.backend.project.reportingapp.dto.response.LaborantResponse;
import org.backend.project.reportingapp.entity.Laborant;
import org.backend.project.reportingapp.entity.Role;
import org.backend.project.reportingapp.entity.User;
import org.backend.project.reportingapp.service.Abstract.LaborantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class LaborantServiceImpl implements LaborantService {

    private final LaborantRepository laborantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Override
    public LaborantResponse createLaborant(LaborantDto laborantDto) {
        if (laborantDto.getFirstName() == null || laborantDto.getFirstName().isEmpty()) {
            log.error("Laborant's first name cannot be null or empty.");
            throw new IllegalArgumentException("Laborant's first name cannot be null or empty.");
        }
        if (laborantDto.getLastName() == null || laborantDto.getLastName().isEmpty()) {
            log.error("Laborant's last name cannot be null or empty.");
            throw new IllegalArgumentException("Laborant's last name cannot be null or empty.");
        }
        if (laborantDto.getHospitalId() == null || laborantDto.getHospitalId().isEmpty()) {
            log.error("Laborant's hospital identity id cannot be null or empty.");
            throw new IllegalArgumentException("Laborant's hospital identity id cannot be null or empty.");
        }

        // Check if a laborant with the same hospital ID already exists
        Optional<Laborant> existingLaborant = laborantRepository.findByHospitalId(laborantDto.getHospitalId());
        if (existingLaborant.isPresent()) {
            log.error("A laborant with this hospital ID already exists.");
            throw new IllegalArgumentException("A laborant with this hospital ID already exists.");
        }
        Role laborantRole = roleRepository.findById(3L).orElseThrow(() ->
                new IllegalArgumentException("Laborant role not found"));
        if (!laborantDto.getUser().getRoleIds().contains(3L)) {
            log.error("Laborant role must be assigned.");
            throw new IllegalArgumentException("Laborant role must be assigned.");
        }
        if (laborantDto.getUser().getRoleIds().size() > 1) {
            log.error("Laborant can only have the laborant role.");
            throw new IllegalArgumentException("Laborant can only have the laborant role.");
        }

        if (userRepository.findByUsername(laborantDto.getUser().getUsername()).isPresent()) {
            log.error("A user with this username already exists.");
            throw new IllegalArgumentException("A user with this username already exists.");
        }
        User user = User.builder()
                .firstName(laborantDto.getFirstName())
                .lastName(laborantDto.getLastName())
                .username(laborantDto.getUser().getUsername())
                .password(passwordEncoder.encode(laborantDto.getUser().getPassword()))
                .roles(Collections.singletonList(laborantRole)).build();

        userRepository.save(user);
        log.info("User who has laborant role created and saved: {}", user);

        Laborant laborant = new Laborant();
        laborant.setHospitalId(laborantDto.getHospitalId());
        laborant.setFirstName(laborantDto.getFirstName());
        laborant.setLastName(laborantDto.getLastName());
        laborant.setUser(user);

        laborantRepository.save(laborant);
        log.info("Laborant created and saved: {}", laborant);

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
                () -> {
                    log.error("Laborant not found with id: {}", id);
                    return new NullPointerException(String.format("Laborant not found with id: %s", id));
                });

        boolean isUpdated = false;

        if (!laborant.getFirstName().equals(laborantDto.getFirstName())) {
            log.trace("Updating first name from {} to {}", laborant.getFirstName(), laborantDto.getFirstName());
            laborant.setFirstName(laborantDto.getFirstName());
            isUpdated = true;
        }
        if (!laborant.getLastName().equals(laborantDto.getLastName())) {
            log.trace("Updating last name from {} to {}", laborant.getLastName(), laborantDto.getLastName());
            laborant.setLastName(laborantDto.getLastName());
            isUpdated = true;
        }
        if (!laborant.getHospitalId().equals(laborantDto.getHospitalId())) {
            log.trace("Updating hospital ID from {} to {}", laborant.getHospitalId(), laborantDto.getHospitalId());
            laborant.setHospitalId(laborantDto.getHospitalId());
            isUpdated = true;
        }
        if (!isUpdated) {
            log.error("No fields have been updated. Please provide different values.");
            throw new IllegalArgumentException("No fields have been updated. Please provide different values.");
        }
        laborantRepository.save(laborant);
        log.info("Laborant updated and saved: {}", laborant);

        return LaborantResponse.Convert(laborant);
    }

    @Override
    public void deleteLaborant(Long id) {
        Laborant laborant = laborantRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Laborant not found with id: {}", id);
                    return new NullPointerException(String.format("Laborant not found with id: %s", id));
                });

        laborantRepository.delete(laborant);
        log.info("Laborant with id: {} deleted", id);
    }
}
