package org.backend.project.reportingapp.service.Abstract;

import org.backend.project.reportingapp.dto.request.UserRegisterRequest;
import org.backend.project.reportingapp.dto.request.UserUpdateRequest;
import org.backend.project.reportingapp.dto.response.UserViewResponse;

import java.util.List;

public interface UserService {
    List<UserViewResponse> getAllUsers(int page, int size, String sortBy, String sortOrder, String filter);

    UserViewResponse saveUser(UserRegisterRequest userRegisterRequest);

    UserViewResponse updateUser(Integer id, UserUpdateRequest userUpdateRequest);

    void deleteUser(Integer id);
}
