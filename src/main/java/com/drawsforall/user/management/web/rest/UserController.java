package com.drawsforall.user.management.web.rest;

import com.drawsforall.user.management.business.UserService;
import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER_CREATE", "ROLE_ANONYMOUS"})
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity
                .created(UriComponentsBuilder.fromPath("/users/{id}").buildAndExpand(createdUser.getId()).toUri())
                .body(createdUser);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedUsersDTO> getUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(userService.getUsers(page, size));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = "/lookup", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> lookupUser(
            @RequestParam(name = "by", defaultValue = "id") String by,
            @RequestParam(name = "value") String value
    ) {
        return ResponseEntity.ok(userService.lookupUser(by, value));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER_UPDATE"})
    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> fieldsToUpdate
    ) {
        return ResponseEntity.ok(userService.updateUser(id, fieldsToUpdate));
    }
}
