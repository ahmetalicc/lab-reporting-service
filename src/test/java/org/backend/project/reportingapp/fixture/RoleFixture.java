package org.backend.project.reportingapp.fixture;

import org.backend.project.reportingapp.entity.Role;

public class RoleFixture extends Fixture<Role>{
    UserFixture userFixture = new UserFixture();
    public Role createRole(){
        Role role = new Role();
        role.setId(faker.number().randomNumber());
        role.setName(faker.gameOfThrones().character());
        role.setUsers(userFixture.createUserList());
        return role;
    }
}
