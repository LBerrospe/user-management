package com.drawsforall.user.management.business;

import com.drawsforall.user.management.business.exception.UserNotFoundException;
import com.drawsforall.user.management.persistence.UserRepository;
import com.drawsforall.user.management.persistence.entity.User;
import com.drawsforall.user.management.web.rest.UserController;
import com.drawsforall.user.management.web.rest.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User addFieldsToUpdateInExistingUser(User existingUser, Map<String, Object> fieldsToUpdate) {
        log.debug("Adding fields {} to update in existing user {}", fieldsToUpdate, existingUser);
        Map<String, Object> existingUserMap = objectMapper.convertValue(existingUser, Map.class);
        existingUserMap.putAll(fieldsToUpdate);
        log.debug("Added fields to update in existing user {}", existingUser);
        return objectMapper.convertValue(existingUserMap, User.class);
    }

    public Resources<UserDTO> getUsers() {
        log.debug("Fetching users");
        List<User> users = userRepository.findAll();
        log.debug("Fetched {} users", users.size());
        return createUserDTOResource(userMapper.toUserDTO(users));
    }

    public Resource<UserDTO> getUser(Long id) {
        log.debug("Fetching user {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        log.debug("Fetched user {}", id);
        return createUserDTOResource(userMapper.toUserDTO(user));
    }

    public Resource<UserDTO> createUser(UserDTO userDTO) {
        log.debug("Creating user {}", userDTO);
        User user = userMapper.fromUserDTO(userDTO);
        User createdUser = userRepository.save(user);
        log.debug("Created user {}", user);
        return createUserDTOResource(userMapper.toUserDTO(createdUser));
    }

    public Resource<UserDTO> updateUser(Long id, Map<String, Object> fieldsToUpdate) {
        log.debug("Updating user {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User userToUpdate = addFieldsToUpdateInExistingUser(user, fieldsToUpdate);
        User updatedUser = userRepository.save(userToUpdate);
        log.debug("Updated user {}", id);
        return createUserDTOResource(userMapper.toUserDTO(updatedUser));
    }

    private Resource<UserDTO> createUserDTOResource(UserDTO userDTO) {
        Link selfRel = linkTo(methodOn(UserController.class).getUser(userDTO.getId())).withSelfRel();
        Link all = linkTo(methodOn(UserController.class).getUsers()).withRel("all");
        Link update = linkTo(methodOn(UserController.class).updateUser(userDTO.getId(), null)).withRel("update");
        return new Resource<>(userDTO, selfRel, all, update);
    }

    private Resources<UserDTO> createUserDTOResource(List<UserDTO> userDTOs) {
        Link selfRel = linkTo(methodOn(UserController.class).getUsers()).withSelfRel();
        return new Resources<>(userDTOs, selfRel);
    }
}
