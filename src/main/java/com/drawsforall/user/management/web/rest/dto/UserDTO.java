package com.drawsforall.user.management.web.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO extends ResourceSupport {

    private Long userId;

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

    private String display;

    private List<String> roles;
}
