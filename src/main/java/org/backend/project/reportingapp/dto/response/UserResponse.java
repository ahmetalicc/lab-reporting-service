package org.backend.project.reportingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.project.reportingapp.entity.Role;
import org.backend.project.reportingapp.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String name;

    private String username;

    private List<String> roles;
    public static UserResponse Convert(User user){
        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }
}
