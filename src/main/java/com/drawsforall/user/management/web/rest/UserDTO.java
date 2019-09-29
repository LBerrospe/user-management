package com.drawsforall.user.management.web.rest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    private String email;
    private String password;
}
