package org.backend.project.reportingapp.service.concrete;

import org.backend.project.reportingapp.dao.LaborantRepository;
import org.backend.project.reportingapp.dao.RoleRepository;
import org.backend.project.reportingapp.dao.UserRepository;
import org.backend.project.reportingapp.dto.request.LaborantDto;
import org.backend.project.reportingapp.dto.response.LaborantResponse;
import org.backend.project.reportingapp.entity.Laborant;
import org.backend.project.reportingapp.entity.Role;
import org.backend.project.reportingapp.entity.User;
import org.backend.project.reportingapp.fixture.LaborantFixture;
import org.backend.project.reportingapp.service.Concrete.LaborantServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaborantServiceImplTest {

    @Mock
    private LaborantRepository laborantRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private LaborantServiceImpl laborantService;
    @Captor
    private ArgumentCaptor<Laborant> laborantCaptor;

    @DisplayName("The test when call with valid laborant information should create laborant and save it to the database.")
    @Test
    void createLaborant_whenCallWithValidLaborantInformation_ShouldCreateLaborant(){
        // Arrange
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();

        Role laborantRole = new Role();
        laborantRole.setId(3L);
        when(roleRepository.findById(3L)).thenReturn(Optional.of(laborantRole));
        when(userRepository.findByUsername(laborantDto.getUser().getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(laborantDto.getUser().getPassword())).thenReturn("encodedPassword");

        Laborant laborant = laborantFixture.createLaborant();
        when(laborantRepository.save(any(Laborant.class))).thenReturn(laborant);

        // Act
        LaborantResponse response = laborantService.createLaborant(laborantDto);

        // Assert
        assertNotNull(response);
        assertEquals(laborantDto.getFirstName(), response.getFirstName());
        assertEquals(laborantDto.getLastName(), response.getLastName());
        verify(laborantRepository, times(1)).save(any(Laborant.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
    @DisplayName("The test when firstname null or empty should throw IllegalArgumentException.")
    @Test
    void createLaborant_whenFirstnameNullOrEmpty_ShouldThrowIllegalArgumentException(){
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        laborantDto.setFirstName(null);

        assertThatThrownBy(()-> laborantService.createLaborant(laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Laborant's first name cannot be null or empty.");

        verify(laborantRepository, never()).save(any(Laborant.class));
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when lastname null or empty should throw IllegalArgumentException.")
    @Test
    void createLaborant_whenLastnameNullOrEmpty_ShouldThrowIllegalArgumentException(){
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        laborantDto.setLastName(null);

        assertThatThrownBy(()-> laborantService.createLaborant(laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Laborant's last name cannot be null or empty.");

        verify(laborantRepository, never()).save(any(Laborant.class));
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when hospital identity id null or empty should throw IllegalArgumentException.")
    @Test
    void createLaborant_whenHospitalIdNullOrEmpty_ShouldThrowIllegalArgumentException(){
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        laborantDto.setHospitalId(null);

        assertThatThrownBy(()-> laborantService.createLaborant(laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Laborant's hospital identity id cannot be null or empty.");

        verify(laborantRepository, never()).save(any(Laborant.class));
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when laborant already exists in database should throw IllegalArgumentException")
    @Test
    void createLaborant_whenLaborantAlreadyExistsInDB_ShouldThrowIllegalArgumentException(){
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        when(laborantRepository.findByHospitalId(laborantDto.getHospitalId())).thenReturn(Optional.of(new Laborant()));

        assertThatThrownBy(()-> laborantService.createLaborant(laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("A laborant with this hospital ID already exists.");

        verify(laborantRepository, never()).save(any(Laborant.class));
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when laborant role not found in database should throw IllegalArgumentException")
    @Test
    void createLaborant_whenRoleNotFound_ShouldThrowIllegalArgumentException(){
        // Arrange
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        when(roleRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> laborantService.createLaborant(laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Laborant role not found");

        verify(laborantRepository, never()).save(any(Laborant.class));
        verify(userRepository, never()).save(any(User.class));

    }
    @DisplayName("The test when laborant role not assigned should throw IllegalArgumentException")
    @Test
    void createLaborant_whenLaborantRoleNotAssigned_ShouldThrowIllegalArgumentException(){
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        laborantDto.getUser().setRoleIds(Collections.singletonList(1L));
        Role laborantRole = new Role();
        laborantRole.setId(3L);
        when(roleRepository.findById(3L)).thenReturn(Optional.of(laborantRole));

        assertThatThrownBy(()-> laborantService.createLaborant(laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Laborant role must be assigned.");

        verify(laborantRepository, never()).save(any(Laborant.class));
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when unnecessary roles assigned to the laborant should throw IllegalArgumentException")
    @Test
    void createLaborant_whenMultipleRoleAssigned_ShouldThrowIllegalArgumentException(){
        // Arrange
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        laborantDto.getUser().setRoleIds(Arrays.asList(3L, 2L));

        Role laborantRole = new Role();
        laborantRole.setId(3L);
        when(roleRepository.findById(3L)).thenReturn(Optional.of(laborantRole));

        assertThatThrownBy(()-> laborantService.createLaborant(laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("Laborant can only have the laborant role.");
        verify(laborantRepository, never()).save(any(Laborant.class));
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when username already exists in database should throw IllegalArgumentException")
    @Test
    void createLaborant_whenUsernameAlreadyExistsInDB_ShouldThrowIllegalArgumentException(){
        // Arrange
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        when(userRepository.findByUsername(laborantDto.getUser().getUsername())).thenReturn(Optional.of(new User()));

        Role laborantRole = new Role();
        laborantRole.setId(3L);
        when(roleRepository.findById(3L)).thenReturn(Optional.of(laborantRole));

        assertThatThrownBy(()-> laborantService.createLaborant(laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("A user with this username already exists.");
        verify(laborantRepository, never()).save(any(Laborant.class));
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when call with page size sortBy sortOrder and filter parameters that should return list of LaborantResponse with pagination")
    @ParameterizedTest
    @ValueSource(strings = {"test", "asd", "123", "ahmet"})
    void getAllLaborants_whenCallWithFilter_ShouldReturnListOfLaborantResponseWithPagination(String filter){
        LaborantFixture laborantFixture = new LaborantFixture();
        List<Laborant> laborantList = laborantFixture.createLaborantList();
        Page<Laborant> pagedProducts = new PageImpl<>(laborantList);

        when(laborantRepository.findLaborantsByFilter(eq(filter), any(Pageable.class)))
                .thenReturn(pagedProducts);

        List<LaborantResponse> actual = laborantService.getAllLaborants(0, 10, "name", "asc", filter);
        List<LaborantResponse> expected = pagedProducts.stream()
                .map(LaborantResponse::Convert)
                .toList();

        assertEquals(actual, expected);
        assertNotNull(filter);
        verify(laborantRepository, times(1)).findLaborantsByFilter(anyString(), any(Pageable.class));
    }
    @DisplayName(" The test when call with page size sortBy and sortOrder parameters that should return list of LaborantResponse with pagination")
    @Test
    void getAllLaborants_whenCallWithoutFilter_ShouldReturnListOfLaborantResponseWithPagination(){
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortOrder = "asc";

        LaborantFixture laborantFixture = new LaborantFixture();
        List<Laborant> laborantList = laborantFixture.createLaborantList();
        Page<Laborant> pagedProducts = new PageImpl<>(laborantList);
        when(laborantRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy).ascending())))
                .thenReturn(pagedProducts);

        // Act
        List<LaborantResponse> actual = laborantService.getAllLaborants(page, size, sortBy, sortOrder, null);
        List<LaborantResponse> expected = pagedProducts.stream()
                .map(LaborantResponse::Convert)
                .toList();

        // Assert
        assertEquals(actual, expected);
        verify(laborantRepository, times(1)).findAll(any(Pageable.class));
    }
    @DisplayName("The test when call without filter that should sorts Laborants in descending order when sort order is descending")
    @Test
    void getAllLaborants_whenCallWithoutFilter_ShouldSortsProductsDescending_whenSortOrderIsDescending(){
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortOrder = "desc";

        LaborantFixture laborantFixture = new LaborantFixture();
        List<Laborant> laborantList = laborantFixture.createLaborantList();
        Page<Laborant> pagedProducts = new PageImpl<>(laborantList);
        when(laborantRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy).descending())))
                .thenReturn(pagedProducts);

        // Act
        List<LaborantResponse> actual = laborantService.getAllLaborants(page, size, sortBy, sortOrder, null);
        List<LaborantResponse> expected = pagedProducts.stream()
                .map(LaborantResponse::Convert)
                .toList();

        // Assert
        assertEquals(actual, expected);
        verify(laborantRepository, times(1)).findAll(any(Pageable.class));
    }
    @DisplayName("The test when laborant exists and fields are updated should update laborant")
    @Test
    void updateLaborant_whenLaborantExistsAndFieldsUpdated_ShouldUpdateLaborant() {
        Long laborantId = 1L;
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        Laborant existingLaborant = laborantFixture.createLaborant();

        when(laborantRepository.findById(laborantId)).thenReturn(Optional.of(existingLaborant));

        Laborant updatedLaborant = new Laborant();
        updatedLaborant.setId(laborantId);
        updatedLaborant.setFirstName(laborantDto.getFirstName());
        updatedLaborant.setLastName(laborantDto.getLastName());
        updatedLaborant.setHospitalId(laborantDto.getHospitalId());

        when(laborantRepository.save(any(Laborant.class))).thenReturn(updatedLaborant);

        LaborantResponse response = laborantService.updateLaborant(laborantId, laborantDto);

        verify(laborantRepository).save(laborantCaptor.capture());
        assertThat(laborantCaptor.getValue().getFirstName()).isEqualTo(laborantDto.getFirstName());
        assertThat(laborantCaptor.getValue().getLastName()).isEqualTo(laborantDto.getLastName());
        assertThat(laborantCaptor.getValue().getHospitalId()).isEqualTo(laborantDto.getHospitalId());
        assertThat(response).isEqualTo(LaborantResponse.Convert(updatedLaborant));
    }

    @DisplayName("The test when laborant not found with given id should throw NullPointerException")
    @Test
    void updateLaborant_whenLaborantNotFound_ShouldThrowNullPointerException(){
        //Arrange
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        Laborant laborant = laborantFixture.createLaborant();
        Long id = laborant.getId();

        when(laborantRepository.findById(id)).thenReturn(Optional.empty());
        //Act&Assert
        assertThatThrownBy(()-> laborantService.updateLaborant(id, laborantDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(String.format("Laborant not found with id: %s", id));
        verify(laborantRepository, never()).save(any(Laborant.class));
    }

    @DisplayName("The test when no fields of laborant updated should throw NullPointerException")
    @Test
    void updateLaborant_whenNoFieldsUpdated_ShouldThrowIllegalArgumentException() {
        // Arrange
        Long laborantId = 1L;
        LaborantFixture laborantFixture = new LaborantFixture();
        LaborantDto laborantDto = laborantFixture.createLaborantDto();
        Laborant existingLaborant = new Laborant();
        existingLaborant.setId(laborantId);
        existingLaborant.setFirstName(laborantDto.getFirstName());
        existingLaborant.setLastName(laborantDto.getLastName());
        existingLaborant.setHospitalId(laborantDto.getHospitalId());

        when(laborantRepository.findById(laborantId)).thenReturn(Optional.of(existingLaborant));

        // Act & Assert
        assertThatThrownBy(()-> laborantService.updateLaborant(laborantId, laborantDto))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("No fields have been updated. Please provide different values.");
        verify(laborantRepository, never()).save(any(Laborant.class));
    }
    @DisplayName("The test when laborant exists in database should delete laborant")
    @Test
    void deleteLaborant_whenLaborantExists_ShouldDeleteLaborant(){
        // Arrange
        Long laborantId = 1L;
        LaborantFixture laborantFixture = new LaborantFixture();
        Laborant laborant = laborantFixture.createLaborant();

        when(laborantRepository.findById(laborantId)).thenReturn(Optional.of(laborant));
        // Act
        laborantService.deleteLaborant(laborantId);
        // Assert
        verify(laborantRepository, times(1)).delete(laborant);
    }
    @DisplayName("Delete laborant when laborant does not exist")
    @Test
    void deleteLaborant_whenLaborantDoesNotExist_ShouldThrowNullPointerException() {
        //Arrange
        Long laborantId = 1L;
        when(laborantRepository.findById(laborantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(()-> laborantService.deleteLaborant(laborantId))
                .isInstanceOf(NullPointerException.class)
                        .hasMessageContaining(String.format("Laborant not found with id: %s", laborantId));
        verify(laborantRepository, never()).delete(any(Laborant.class));
    }

}


