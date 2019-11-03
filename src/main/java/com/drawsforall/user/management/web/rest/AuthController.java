package com.drawsforall.user.management.web.rest;

import com.drawsforall.user.management.business.UserService;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserService userService;

    //Login mapping is created in the class AuthorizationServerConfig

    @PostMapping(value = "/register", produces = APPLICATION_JSON_VALUE)
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.registerNewUser(userDTO);
    }
}
