package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class TestDataInit {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public TestDataInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {

        Role adminRole = new Role(Role.RoleType.ROLE_ADMIN);
        Role userRole = new Role(Role.RoleType.ROLE_USER);

        roleService.saveRole(adminRole);
        roleService.saveRole(userRole);

        if (userService.allUsers().isEmpty()) {

            userService.saveUser(new User("user", "user" //user
                    , new HashSet<>(Set.of(userRole)), "Jackie", "Chan", (byte) 69, 100));

            userService.saveUser(new User("admin-user", "admin-user" //admin-user
                    , new HashSet<>(Set.of(adminRole, userRole)),"Bruce", "Lee", (byte) 32, -100));

            userService.saveUser(new User("user1", "user1" //user1
                    , new HashSet<>(Set.of(userRole)),"Steven", "Seagal", (byte) 71, 10));

            userService.saveUser(new User("admin", "admin" //admin
                    , new HashSet<>(Set.of(adminRole)),"Gordon", "Liu", (byte) 72, -200));
        }
    }
}
