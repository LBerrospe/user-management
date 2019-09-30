package com.drawsforall.user.management.web.rest;

import com.drawsforall.user.management.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Resources<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Resource<UserDTO> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public Resource<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Resource<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> fieldsToUpdate
    ) {
        return userService.updateUser(id, fieldsToUpdate);
    }



}
