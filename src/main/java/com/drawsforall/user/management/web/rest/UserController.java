package com.drawsforall.user.management.web.rest;

import com.drawsforall.user.management.business.UserService;
import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public PagedUsersDTO getUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return userService.getUsers(page, size);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public UserDTO updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> fieldsToUpdate
    ) {
        return userService.updateUser(id, fieldsToUpdate);
    }

    @GetMapping(value = "/search-by-email/{email}", produces = APPLICATION_JSON_VALUE)
    public UserDTO getUsers(@PathVariable String email) {
        return userService.getUser(email);
    }
}
