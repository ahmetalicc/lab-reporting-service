package org.backend.project.reportingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.project.reportingapp.entity.Laborant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaborantResponse {
    private String firstName;
    private String lastName;
    private String hospitalId;

    public static LaborantResponse Convert(Laborant laborant){
        return LaborantResponse.builder()
                .firstName(laborant.getFirstName())
                .lastName(laborant.getLastName())
                .hospitalId(laborant.getHospitalId())
                .build();
    }
}
