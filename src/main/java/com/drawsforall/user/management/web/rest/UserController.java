package com.drawsforall.user.management.web.rest;

import com.drawsforall.user.management.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Resource<UserDTO> getUser(@PathVariable Long id) {
        UserDTO user = userService.getUser(id);
        Resource<UserDTO> resource = new Resource<>(user);
        resource.add(linkTo(methodOn(this.getClass()).getUser(id)).withSelfRel());
        return resource;
    }
}
