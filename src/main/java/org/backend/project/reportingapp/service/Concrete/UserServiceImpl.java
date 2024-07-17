package org.backend.project.reportingapp.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.dao.RoleRepository;
import org.backend.project.reportingapp.dao.UserRepository;
import org.backend.project.reportingapp.dto.request.UserRegisterRequest;
import org.backend.project.reportingapp.dto.request.UserUpdateRequest;
import org.backend.project.reportingapp.dto.response.UserViewResponse;
import org.backend.project.reportingapp.entity.Role;
import org.backend.project.reportingapp.entity.User;
import org.backend.project.reportingapp.service.Abstract.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public List<UserViewResponse> getAllUsers(int page, int size, String sortBy, String sortOrder, String filter) {
        Sort sort = Sort.by(sortBy).ascending();
        if ("desc".equals(sortOrder)) {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users;
        if(filter == null || filter.trim().isEmpty()){
            users = userRepository.findAll(pageable);
        }
        else {
            users = userRepository.findUsersByFilter(filter, pageable);
        }

        return users.stream()
                .map(UserViewResponse::Convert)
                .collect(Collectors.toList());
    }

    @Override
    public UserViewResponse saveUser(UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest.getRoleIds() == null || userRegisterRequest.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("RoleId list cannot be null or empty");
        }

        List<Role> roles = userRegisterRequest.getRoleIds().stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("No roles found for the provided RoleId list");
        }


        User user = User.builder()
                .name(userRegisterRequest.getName())
                .username(userRegisterRequest.getUsername())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .roles(roles).build();

        userRepository.save(user);

        return UserViewResponse.Convert(user);
    }

    @Override
    public UserViewResponse updateUser(Integer id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException(String.format("User not found with id(%s) that is provided:", id)));

        if(userRepository.existsByUsername(userUpdateRequest.getUsername())){
            throw new IllegalStateException(String.format("Username %s is already in the database.",
                    userUpdateRequest.getUsername()));
        }

        user.setName(userUpdateRequest.getName());
        user.setUsername(userUpdateRequest.getUsername());
        userRepository.save(user);

        return UserViewResponse.Convert(user);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new NullPointerException(String.format("User not found with id: %s", id)));

        userRepository.delete(user);
    }


}
