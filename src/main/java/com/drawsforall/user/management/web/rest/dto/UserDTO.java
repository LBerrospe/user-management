package com.drawsforall.user.management.web.rest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
public class UserDTO extends ResourceSupport {

    private Long userId;

    @Size(max = 128, message = "Email must contain less than 128 characters")
    @Email(message = "Please provide a valid email.")
    @NotBlank(message = "Please provide an email")
    private String email;

    @Size(min = 5, max = 255, message = "Password must have at least 5 characters")
    @NotBlank(message = "Please provide a password")
    private String password;

    @Size(max = 32, message = "First name must contain less than 32 characters")
    @NotBlank(message = "Please provide a first name")
    private String firstName;

    @Size(max = 32, message = "Last name must contain less than 32 characters")
    @NotBlank(message = "Please provide a last name")
    private String lastName;

    private String display;

    private List<String> roles;
}
