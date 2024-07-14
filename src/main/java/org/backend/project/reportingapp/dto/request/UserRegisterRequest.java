package org.backend.project.reportingapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {

    private String name;

    private String username;

    private String password;
    @NotEmpty(message = "RoleId list cannot be empty")
    private List<Long> roleIds;
}
