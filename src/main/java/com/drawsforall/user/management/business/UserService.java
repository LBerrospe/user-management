package com.drawsforall.user.management.business;

import com.drawsforall.user.management.business.exception.UserNotFoundException;
import com.drawsforall.user.management.persistence.RoleRepository;
import com.drawsforall.user.management.persistence.UserRepository;
import com.drawsforall.user.management.persistence.entity.Role;
import com.drawsforall.user.management.persistence.entity.RoleType;
import com.drawsforall.user.management.persistence.entity.User;
import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Transactional
@Service
public class UserService {

    private final String ID = "id";
    private final String EMAIL = "email";

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private UserMapper userMapper;

    private ObjectMapper objectMapper;

    private BCryptPasswordEncoder passwordEncoder;

    private AuthenticationService authenticationService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, ObjectMapper objectMapper, BCryptPasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    public PagedUsersDTO getUsers(int page, int size) {
        log.debug("Fetching users");
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        log.debug("Fetched {} users", users.getNumberOfElements());
        return userMapper.toPagedUsersDTO(users);
    }

    public UserDTO lookupUser(String by, String value) {
        log.debug("Fetching user by {} with {}", by, value);
        User user;
        switch (by) {
            case ID:
                Long id = Long.parseLong(value);
                user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
                break;

            case EMAIL:
                user = userRepository.findByEmailIgnoreCase(value).orElseThrow(() -> new UserNotFoundException(value));
                break;

            default:
                throw new IllegalArgumentException("Could not find user by " + by);
        }

        log.debug("Fetched user by {} with {}", by, value);
        return userMapper.toUserDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        userRepository.findByEmailIgnoreCase(userDTO.getEmail()).ifPresent(user -> {
            throw new IllegalArgumentException("Duplicate email " + userDTO.getEmail());
        });
        User user = userMapper.fromUserDTO(userDTO);
        log.debug("Creating user {}", userDTO);
        Collection<? extends GrantedAuthority> authorities = authenticationService.getAuthentication().getAuthorities();
        Set<Role> roles = authorities.contains(RoleType.ROLE_ADMIN.toString())
                ? roleRepository.findAllByNameIn(userDTO.getRoles())
                : roleRepository.findAllByNameIn(Collections.singletonList(RoleType.ROLE_USER.name()));
        user.setRoles(roles);
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

    private User addFieldsToUpdateInExistingUser(User existingUser, Map<String, Object> fieldsToUpdate) {
        log.debug("Adding fields {} to update in existing user {}", fieldsToUpdate, existingUser);
        Map<String, Object> existingUserMap = objectMapper.convertValue(existingUser, Map.class);
        existingUserMap.putAll(fieldsToUpdate);
        existingUserMap.put("roles", roleRepository.findAllByNameIn((List<String>) fieldsToUpdate.get("roles")));
        existingUserMap.put("password", passwordEncoder.encode((CharSequence) fieldsToUpdate.get("password")));
        log.debug("Added fields to update in existing user {}", existingUser);
        return objectMapper.convertValue(existingUserMap, User.class);
    }
}
