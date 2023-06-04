package com.elves.dscommerce.dto;

import com.elves.dscommerce.entities.User;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;

    private List<String> roles = new ArrayList<>();

    public UserDTO(){

    }
    public UserDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        phone = entity.getPhone();
        birthDate = entity.getBirthDate();
        entity.getAuthorities().forEach(x -> roles.add(x.getAuthority()));
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<String> getRoles() {
        return roles;
    }
}
