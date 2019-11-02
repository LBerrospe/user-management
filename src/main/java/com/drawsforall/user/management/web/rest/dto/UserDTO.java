package com.drawsforall.user.management.web.rest.dto;

import com.drawsforall.user.management.persistence.entity.Role;
import com.drawsforall.user.management.persistence.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@ToString
public class UserDTO extends ResourceSupport {

    private Long userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> role;
    private String display;





}
