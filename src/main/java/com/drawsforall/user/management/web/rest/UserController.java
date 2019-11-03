package com.drawsforall.user.management.web.rest;

import com.drawsforall.user.management.business.AuthenticationService;
import com.drawsforall.user.management.business.UserService;
import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    @Secured("ROLE_ADMIN")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public PagedUsersDTO getUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        log.debug(String.format("received request to list user %s", authenticationService.getAuthentication().getPrincipal()));
        return userService.getUsers(page, size);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public UserDTO updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> fieldsToUpdate
    ) {
        return userService.updateUser(id, fieldsToUpdate);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/search-by-email/{email}", produces = APPLICATION_JSON_VALUE)
    public UserDTO getUsers(@PathVariable String email) {
        return userService.getUser(email);
    }
}
