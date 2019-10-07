package com.drawsforall.user.management.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class UserDTO extends ResourceSupport {

    private Long userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String display;
}
