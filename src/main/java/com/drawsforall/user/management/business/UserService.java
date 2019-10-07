package com.drawsforall.user.management.business;

import com.drawsforall.user.management.business.exception.UserNotFoundException;
import com.drawsforall.user.management.persistence.UserRepository;
import com.drawsforall.user.management.persistence.entity.User;
import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    public PagedUsersDTO getUsers(int page, int size) {
        log.debug("Fetching users");
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        log.debug("Fetched {} users", users.getNumberOfElements());
        return userMapper.toPagedUsersDTO(users);
    }

    public UserDTO getUser(Long id) {
        log.debug("Fetching user {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        log.debug("Fetched user {}", id);
        return userMapper.toUserDTO(user);
    }

    public UserDTO getUser(String email) {
        log.debug("Fetching user {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        log.debug("Fetched user {}", email);
        return userMapper.toUserDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        log.debug("Creating user {}", userDTO);
        User user = userMapper.fromUserDTO(userDTO);
        User createdUser = userRepository.save(user);
        log.debug("Created user {}", user);
        return userMapper.toUserDTO(createdUser);
    }

    public UserDTO updateUser(Long id, Map<String, Object> fieldsToUpdate) {
        log.debug("Updating user {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User userToUpdate = addFieldsToUpdateInExistingUser(user, fieldsToUpdate);
        User updatedUser = userRepository.save(userToUpdate);
        log.debug("Updated user {}", id);
        return userMapper.toUserDTO(updatedUser);
    }
}
