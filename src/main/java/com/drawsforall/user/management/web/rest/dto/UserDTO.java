package com.drawsforall.user.management.web.rest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;


@Getter
@Setter
@ToString
public class UserDTO extends ResourceSupport {

    private Long userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private String display;
}
