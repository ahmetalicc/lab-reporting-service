package org.backend.project.reportingapp.fixture;

import org.backend.project.reportingapp.dto.request.UserRegisterRequest;
import org.backend.project.reportingapp.dto.request.UserUpdateRequest;
import org.backend.project.reportingapp.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserFixture extends Fixture<User> {

    public List<User> createUserList() {
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            userList.add(User.builder()
                    .id(faker.number().randomDigit())
                    .firstName(faker.gameOfThrones().character())
                    .lastName(faker.funnyName().name())
                    .username(faker.name().username())
                    .password(faker.internet().password())
                    .roles(new ArrayList<>())
                    .build());
        }

        return userList;
    }
    public User createUser() {
        User user = new User();

        user.setId(faker.number().randomDigit());
        user.setUsername(faker.name().username());
        user.setFirstName(faker.gameOfThrones().character());
        user.setLastName(faker.funnyName().name());
        user.setPassword(faker.internet().password());
        user.setRoles(new ArrayList<>());

        return user;
    }
    public UserRegisterRequest createUserRegisterRequest(){
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();

        userRegisterRequest.setFirstName(faker.name().fullName());
        userRegisterRequest.setLastName(faker.funnyName().name());
        userRegisterRequest.setUsername(faker.name().username());
        userRegisterRequest.setPassword(faker.internet().password());
        userRegisterRequest.setRoleIds(Collections.singletonList(faker.number().randomNumber()));

        return userRegisterRequest;
    }
    public UserUpdateRequest createUserUpdateRequest(){
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();

        userUpdateRequest.setFirstName(faker.name().fullName());
        userUpdateRequest.setLastName(faker.funnyName().name());
        userUpdateRequest.setUsername(faker.gameOfThrones().character());

        return userUpdateRequest;
    }
}