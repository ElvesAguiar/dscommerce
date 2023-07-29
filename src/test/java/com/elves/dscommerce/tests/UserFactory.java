package com.elves.dscommerce.tests;

import com.elves.dscommerce.entities.Category;
import com.elves.dscommerce.entities.Role;
import com.elves.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createClientUser() {


        User user = new User(1L, "maria", "maria@gmail.com",
                "988888888", LocalDate.of(2001, 07, 25),
                "2a$10$2kHOcpqJi/GtZPf1xSRPRe0MogPyRoPK4hY2cQAMQH2CWSLmJkd5e");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser() {

        User user = new User(2L, "alex", "alex@gmail.com",
                "977777777", LocalDate.of(1987, 12, 13),
                "2a$10$2kHOcpqJi/GtZPf1xSRPRe0MogPyRoPK4hY2cQAMQH2CWSLmJkd5e");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String userName) {
        User user = new User(id, "maria", userName,
                "988888888", LocalDate.of(2001, 07, 25),
                "2a$10$2kHOcpqJi/GtZPf1xSRPRe0MogPyRoPK4hY2cQAMQH2CWSLmJkd5e");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;

    }

    public static User createCustomAdminUser(Long id, String userName) {
        User user = new User(id, "alex", userName,
                "977777777", LocalDate.of(1987, 12, 13),
                "2a$10$2kHOcpqJi/GtZPf1xSRPRe0MogPyRoPK4hY2cQAMQH2CWSLmJkd5e");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;

    }

}