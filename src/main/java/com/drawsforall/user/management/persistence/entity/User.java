package com.drawsforall.user.management.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Please provide a valid email.")
    @NotBlank(message = "Please provide an email")
    @Size(max = 128, message = "Email must contain less than 128 characters")
    private String email;

    @NotBlank(message = "Please provide a password")
    @Size(min = 5, max = 255, message = "Password must have at least 5 characters")
    private String password;

    @NotBlank(message = "Please provide a first name")
    @Size(max = 32, message = "First name must contain less than 32 characters")
    private String firstName;

    @NotBlank(message = "Please provide a last name")
    @Size(max = 32, message = "Last name must contain less than 32 characters")
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles;
}
