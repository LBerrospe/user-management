package com.drawsforall.user.management.business;

import com.drawsforall.user.management.business.exception.UserNotFoundException;
import com.drawsforall.user.management.persistence.RoleRepository;
import com.drawsforall.user.management.persistence.UserRepository;
import com.drawsforall.user.management.persistence.entity.Role;
import com.drawsforall.user.management.persistence.entity.User;
import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class UserService implements UserDetailsService {

    private final String ID = "id";
    private final String EMAIL = "email";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //this function is used by SpringSecurity to give the Role.
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException(email));

        Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roleByUserId = user.getRoles();
        final Set<GrantedAuthority> authorities = roleByUserId.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString().toUpperCase())).collect(Collectors.toSet());
        return authorities;
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
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(roleRepository.findAllByNameIn(userDTO.getRoles()));
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
