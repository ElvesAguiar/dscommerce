package com.elves.dscommerce.tests;

import com.elves.dscommerce.entities.Role;
import com.elves.dscommerce.entities.User;
import com.elves.dscommerce.projections.UserDetailsProjection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDetailsFactory {

    public static List<UserDetailsProjection> createCustomClientUser(String userName) {
        List<UserDetailsProjection> list = new ArrayList<>();
        list.add(new UserDetailsImpl(userName, "123", 1L, "ROLE_CLIENT"));
        return list;
    }

    public static List<UserDetailsProjection> createCustomAdminUser(String userName) {
        List<UserDetailsProjection> list = new ArrayList<>();
        list.add(new UserDetailsImpl(userName, "123", 2L, "ROLE_ADMIN"));
        return list;
    }

    public static List<UserDetailsProjection> createCustomClientAdminUser(String userName) {
        List<UserDetailsProjection> list = new ArrayList<>();

        list.add(new UserDetailsImpl(userName, "123", 1L, "ROLE_CLIENT"));

        list.add(new UserDetailsImpl(userName, "123", 2L, "ROLE_ADMIN"));


        return list;
    }


}

class UserDetailsImpl implements UserDetailsProjection {


    public String username;
    public String password;
    public Long roleId;
    public String authority;

    public UserDetailsImpl() {

    }

    public UserDetailsImpl(String username, String password, Long roleId, String authority) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.authority = authority;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}