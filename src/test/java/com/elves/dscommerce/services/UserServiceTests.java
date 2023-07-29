package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.UserDTO;
import com.elves.dscommerce.entities.User;
import com.elves.dscommerce.projections.UserDetailsProjection;
import com.elves.dscommerce.repositories.UserRepository;
import com.elves.dscommerce.tests.UserDetailsFactory;
import com.elves.dscommerce.tests.UserFactory;
import com.elves.dscommerce.utils.CustomUserUtil;
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
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;
    @Mock
    private CustomUserUtil util;
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
        Mockito.when(repository.findByEmail(existingUserName)).thenReturn(Optional.of(user));
        Mockito.when(repository.findByEmail(nonExistingUserName)).thenReturn(Optional.empty());


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

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(nonExistingUserName);
        });
    }

    @Test
    public void authenticatedShouldReturnUserWhenUserExists() {
        Mockito.when(util.getLoggedUserName()).thenReturn(existingUserName);


        User result = service.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUserName);

    }

    @Test
    public void authenticatedShouldReturnUsernameNotFoundExceptionWhenUserDoesNotExist() {
        Mockito.doThrow(ClassCastException.class).when(util).getLoggedUserName();
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.authenticated();
        });
    }

    @Test
    public void getMeShouldReturnUserDTOWhenUserAuthenticated() {
        UserService userServiceSpy = Mockito.spy(service);
        Mockito.doReturn(user).when(userServiceSpy).authenticated();

        UserDTO result = userServiceSpy.getMe();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getEmail(), existingUserName);
    }

    @Test
    public void getMeShouldReturnUsernameNotFoundExceptionWhenUserIsNotAuthenticated() {
        UserService userServiceSpy = Mockito.spy(service);

        Mockito.doThrow(UsernameNotFoundException.class).when(userServiceSpy).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDTO result = userServiceSpy.getMe();
        });

    }

}
