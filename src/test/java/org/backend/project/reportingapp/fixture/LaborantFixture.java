package org.backend.project.reportingapp.fixture;

import org.backend.project.reportingapp.dto.request.LaborantDto;
import org.backend.project.reportingapp.dto.request.UserDto;
import org.backend.project.reportingapp.entity.Laborant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaborantFixture extends Fixture<Laborant> {

    UserFixture userFixture = new UserFixture();

    public Laborant createLaborant() {
        Laborant laborant = new Laborant();

        laborant.setId(faker.number().randomNumber());
        laborant.setFirstName(faker.name().firstName());
        laborant.setLastName(faker.name().lastName());
        laborant.setHospitalId(faker.number().digits(10));
        laborant.setUser(userFixture.createUser());

        return laborant;
    }
    public List<Laborant> createLaborantList() {
        List<Laborant> laborants = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            laborants.add(createLaborant());
        }

        return laborants;
    }
    public LaborantDto createLaborantDto() {
        LaborantDto laborantDto = new LaborantDto();

        laborantDto.setFirstName(faker.name().firstName());
        laborantDto.setLastName(faker.name().lastName());
        laborantDto.setHospitalId(faker.number().digits(10));

        UserDto userDto = new UserDto();
        userDto.setUsername(faker.name().username());
        userDto.setPassword(faker.internet().password());
        userDto.setRoleIds(Collections.singletonList(3L));

        laborantDto.setUser(userDto);

        return laborantDto;
    }
}
