package org.backend.project.reportingapp.service.concrete;

import org.assertj.core.api.Assertions;
import org.backend.project.reportingapp.dao.RoleRepository;
import org.backend.project.reportingapp.dao.UserRepository;
import org.backend.project.reportingapp.dto.request.UserRegisterRequest;
import org.backend.project.reportingapp.dto.request.UserUpdateRequest;
import org.backend.project.reportingapp.dto.response.UserViewResponse;
import org.backend.project.reportingapp.entity.Role;
import org.backend.project.reportingapp.entity.User;
import org.backend.project.reportingapp.fixture.RoleFixture;
import org.backend.project.reportingapp.fixture.UserFixture;
import org.backend.project.reportingapp.service.Concrete.UserServiceImpl;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @InjectMocks
    private UserServiceImpl userService;

    @DisplayName("The test that when call with page size sortBy sortOrder and filter should return list of UserViewResponse list with pagination")
    @ParameterizedTest
    @ValueSource(strings = {"test", "asd", "123", "ahmet"})
    void getAllUsers_whenCallWithFilter_ShouldReturnUserViewResponseListWithPagination(String filter){
        UserFixture userFixture = new UserFixture();
        List<User> userList = userFixture.createUserList();
        Page<User> pagedUsers = new PageImpl<>(userList);

        when(userRepository.findUsersByFilter(eq(filter), any(Pageable.class))).thenReturn(pagedUsers);

        List<UserViewResponse> actual = userService.getAllUsers(0, 3, "username", "asc", filter);
        List<UserViewResponse> expected = pagedUsers.stream()
                .map(UserViewResponse::Convert)
                .toList();

        assertEquals(actual, expected);
        assertNotNull(filter);

        verify(userRepository, times(1)).findUsersByFilter(anyString(), any(Pageable.class));
    }
    @DisplayName("The test when call with page size sortBy and sortOrder parameters that should return UserViewResponse list with pagination")
    @Test
    void getAllUsers_whenCallWithoutFilter_ShouldReturnUserViewResponseListWithPagination(){
        int page = 0;
        int size = 3;
        String sortBy = "username";
        String sortOrder = "asc";

        UserFixture userFixture = new UserFixture();
        List<User> userList = userFixture.createUserList();
        Page<User> pagedUsers = new PageImpl<>(userList);

        when(userRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy).ascending())))
                .thenReturn(pagedUsers);

        List<UserViewResponse> actual = userService.getAllUsers(page, size, sortBy, sortOrder, null) ;
        List<UserViewResponse> expected = pagedUsers.stream()
                .map(UserViewResponse::Convert)
                .toList();

        assertEquals(actual, expected);

        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }
    @DisplayName("The test when call without filter that should sorts Users in descending order when sort order is descending")
    @Test
    void getAllUsers_whenCallWithoutFilter_ShouldSortsUsersDescending_whenSortOrderIsDescending(){
        int page = 0;
        int size = 10;
        String sortBy = "username";
        String sortOrder = "desc";

        UserFixture userFixture = new UserFixture();
        List<User> userList = userFixture.createUserList();
        Page<User> pagedUsers = new PageImpl<>(userList);
        when(userRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy).descending())))
                .thenReturn(pagedUsers);

        List<UserViewResponse> actual = userService.getAllUsers(page, size, sortBy, sortOrder, null);
        List<UserViewResponse> expected = pagedUsers.stream()
                .map(UserViewResponse::Convert)
                .toList();

        assertEquals(actual, expected);

        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }
    @DisplayName("The test when user saved to the database successfully should return user with UserViewResponse object ")
    @Test
    void saveUser_whenUserSavedToTheDBSuccessfully_ShouldReturnUserViewResponse(){
        UserFixture userFixture = new UserFixture();
        RoleFixture roleFixture = new RoleFixture();
        UserRegisterRequest request = userFixture.createUserRegisterRequest();
        Role role = roleFixture.createRole();

        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        UserViewResponse response = userService.saveUser(request);


        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(request.getLastName());
        assertThat(savedUser.getUsername()).isEqualTo(request.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(response).isEqualTo(UserViewResponse.Convert(savedUser));
        verify(roleRepository, times(1)).findById(anyLong());
    }
    @DisplayName("The test when role id list is null should throw IllegalArgumentException")
    @Test
    void saveUser_whenRoleIdsNull_ShouldThrowIllegalArgumentException(){
        UserFixture userFixture = new UserFixture();
        UserRegisterRequest userRegisterRequest = userFixture.createUserRegisterRequest();
        userRegisterRequest.setRoleIds(null);

        assertThatThrownBy(()-> userService.saveUser(userRegisterRequest))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("RoleId list cannot be null or empty");
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when role id list is empty should throw IllegalArgumentException")
    @Test
    void saveUser_whenRoleIdsEmpty_ShouldThrowIllegalArgumentException(){
        UserFixture userFixture = new UserFixture();
        UserRegisterRequest userRegisterRequest = userFixture.createUserRegisterRequest();
        userRegisterRequest.setRoleIds(Collections.emptyList());

        assertThatThrownBy(()-> userService.saveUser(userRegisterRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("RoleId list cannot be null or empty");
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when role id list contains laborant role should throw IllegalArgumentException")
    @Test
    void saveUser_whenRoleIdsContainsLaborantRole_ShouldThrowIllegalArgumentException(){
        UserFixture userFixture = new UserFixture();
        UserRegisterRequest userRegisterRequest = userFixture.createUserRegisterRequest();
        userRegisterRequest.setRoleIds(Collections.singletonList(3L));

        assertThatThrownBy(()-> userService.saveUser(userRegisterRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Laborant role can not be added here.");
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when no roles found for provided RoleId list should throw IllegalArgumentException")
    @Test
    void saveUser_whenNoRolesFound_ShouldThrowIllegalArgumentException(){
        UserFixture userFixture = new UserFixture();
        UserRegisterRequest userRegisterRequest = userFixture.createUserRegisterRequest();
        when(roleRepository.findById(userRegisterRequest.getRoleIds().get(0))).thenReturn(Optional.empty());

        assertThatThrownBy(()-> userService.saveUser(userRegisterRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No roles found for the provided RoleId list");
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when valid id and UserUpdateRequest parameters should update user and should return UserViewResponse object")
    @Test
    void updateUser_whenValidIdAndUserUpdateRequestParameters_ShouldReturnUserViewResponse(){
        Integer id = 1;
        UserFixture userFixture = new UserFixture();
        UserUpdateRequest request = userFixture.createUserUpdateRequest();
        User user = userFixture.createUser();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserViewResponse response = userService.updateUser(id, request);

        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();
        assertThat(updatedUser.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(updatedUser.getLastName()).isEqualTo(request.getLastName());
        assertThat(updatedUser.getUsername()).isEqualTo(request.getUsername());
        assertThat(response).isEqualTo(UserViewResponse.Convert(updatedUser));

        verify(userRepository, times(1)).findById(id);
    }
    @DisplayName("The test that when user not found with given id should throw NullPointerException")
    @Test
    void updateUser_whenUserNotFoundWithGivenId_ShouldReturnNullPointerException(){
        Integer id = 1;
        UserFixture userFixture = new UserFixture();
        UserUpdateRequest request = userFixture.createUserUpdateRequest();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(()-> userService.updateUser(id, request))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(String.format("User not found with id(%s) that is provided:", id));
    }
    @DisplayName("The test that when user already exist with username should throw IllegalStateException")
    @Test
    void updateUser_whenUserAlreadyExistWithUsername_ShouldThrowIllegalStateException(){
        Integer id = 1;
        UserFixture userFixture = new UserFixture();
        UserUpdateRequest request = userFixture.createUserUpdateRequest();
        User user = userFixture.createUser();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        Assertions.assertThatThrownBy(()-> userService.updateUser(id, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Username %s is already in the database.", request.getUsername()));

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).existsByUsername(request.getUsername());
    }
    @DisplayName("The test when no fields updated should throw IllegalStateException")
    @Test
    void updateUser_whenNoFieldsUpdated_ShouldThrowIllegalStateException(){
        Integer id = 1;
        UserFixture userFixture = new UserFixture();
        User user = userFixture.createUser();
        UserUpdateRequest request = userFixture.createUserUpdateRequest();
        request.setUsername(user.getUsername());
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(()-> userService.updateUser(id, request))
                .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("No fields have been updated. Please provide different values.");
        verify(userRepository, never()).save(any(User.class));
    }
    @DisplayName("The test when call with valid id parameter should delete user from database")
    @Test
    void deleteUser_whenCallWithValidId_ShouldDeleteUserFromDB(){
        Integer id = 1;
        UserFixture userFixture = new UserFixture();
        User user = userFixture.createUser();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).delete(user);
    }
    @DisplayName("The test when call with non existing id parameter should throw NullPointerException")
    @Test
    void deleteUser_whenCallWithNonExistingId_ShouldThrowNullPointerException(){
        Integer id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> userService.deleteUser(id))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining(String.format("User not found with id: %s", id));
    }
}
