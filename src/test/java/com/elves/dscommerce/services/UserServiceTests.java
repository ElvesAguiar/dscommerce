package com.elves.dscommerce.services;

import com.elves.dscommerce.entities.User;
import com.elves.dscommerce.projections.UserDetailsProjection;
import com.elves.dscommerce.repositories.UserRepository;
import com.elves.dscommerce.tests.UserDetailsFactory;
import com.elves.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    String[] list;
    UserDetailsProjection projection;

    private User user;
    String name;
    private String existingUserName;
    private String nonExistingUserName;

    private List<UserDetailsProjection> userDetails;


    @BeforeEach
    void setUp() throws Exception {
        existingUserName = "maria@gmail.com";
        nonExistingUserName = "user@gmail.com";

        user = UserFactory.createCustomClientUser(1L, existingUserName);
        userDetails = UserDetailsFactory.createCustomClientUser(existingUserName);

        Mockito.when(repository.searchUserAndRolesByEmail(existingUserName)).thenReturn(userDetails);
        Mockito.when(repository.searchUserAndRolesByEmail(nonExistingUserName)).thenReturn(new ArrayList<>());

    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUserDoesExit() {
        UserDetails result = service.loadUserByUsername(existingUserName);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUserName);
    }

    @Test
    public void loadUserByUsernameShouldReturnUserNotFoundExceptionWhenUserDoesNotExit() {
        UserDetails result = service.loadUserByUsername(existingUserName);

        Assertions.assertThrows(UsernameNotFoundException.class,() -> {
           service.loadUserByUsername(nonExistingUserName);
        });
    }

}
