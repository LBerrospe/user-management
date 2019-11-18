package com.drawsforall.user.management.business;

import com.drawsforall.user.management.business.exception.UserNotFoundException;
import com.drawsforall.user.management.persistence.entity.RoleType;
import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.drawsforall.user.management.business.AuthenticationService.ROLE_ANONYMOUS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void getUsers() {
        int page = 1;
        int size = 5;
        PagedUsersDTO pagedUsersDTO = userService.getUsers(page, size);

        assertNotNull(pagedUsersDTO);
        assertEquals(page, pagedUsersDTO.getNumber());
        assertEquals(size, pagedUsersDTO.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUsers_NonValidPage() {
        int page = -1;
        int size = 5;
        userService.getUsers(page, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUsers_NonValidPageSize() {
        int page = 1;
        int size = -5;
        userService.getUsers(page, size);
    }

    @Test
    public void lookupUser_ById() {
        String by = "id";
        String value = "1";
        UserDTO fetchedUser = userService.lookupUser(by, value);

        assertNotNull(fetchedUser);
        assertEquals(new Long(value), fetchedUser.getUserId());
    }

    @Test(expected = UserNotFoundException.class)
    public void lookupUser_ByIdNonExistent() {
        String by = "id";
        String value = "-55";
        userService.lookupUser(by, value);
    }

    @Test(expected = NumberFormatException.class)
    public void lookupUser_ByIdInvalidId() {
        String by = "id";
        String value = "a5";
        UserDTO fetchedUser = userService.lookupUser(by, value);
        assertNotNull(fetchedUser);
        assertEquals(new Long(value), fetchedUser.getUserId());
    }

    @Test
    public void lookupUser_ByEmail() {
        String by = "email";
        String value = "yagami.light@deathnote.com";
        UserDTO fetchedUser = userService.lookupUser(by, value);

        assertNotNull(fetchedUser);
        assertTrue(value.equalsIgnoreCase(fetchedUser.getEmail()));
    }

    @Test(expected = UserNotFoundException.class)
    public void lookupUser_ByEmailNonExistent() {
        String by = "email";
        String value = "itachi.uchiha@naruto.com";
        userService.lookupUser(by, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void lookupUser_ByInvalidArgument() {
        String by = "firstName";
        String value = "Yagami";
        userService.lookupUser(by, value);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createUser_NotBlankFields() {
        List<String> rolesToAuthenticate = Arrays.asList(ROLE_ANONYMOUS);

        String email = "";
        String password = "";
        String firstName = "";
        String lastName = "";

        authenticateAs(rolesToAuthenticate);
        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, null);
        userService.createUser(userDTO);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createUser_NotValidEmail() {
        List<String> rolesToAuthenticate = Arrays.asList(ROLE_ANONYMOUS);

        String email = "l.lawliet";
        String password = "LLawliet";
        String firstName = "L";
        String lastName = "Lawliet";

        authenticateAs(rolesToAuthenticate);
        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, null);
        userService.createUser(userDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUser_DuplicatedEmail() {
        List<String> rolesToAuthenticate = Arrays.asList(ROLE_ANONYMOUS);

        String email = "yagami.light@deathnote.com";
        String password = "YagamiLight";
        String firstName = "Yagami";
        String lastName = "Light";

        authenticateAs(rolesToAuthenticate);
        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, null);
        userService.createUser(userDTO);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createUser_NotValidSize() {
        List<String> rolesToAuthenticate = Arrays.asList(ROLE_ANONYMOUS);

        String email = "l.lawliet@deathnote.com";
        String password = "LLa";
        String firstName = "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL";
        String lastName = "LawlietLawlietLawlietLawlietLawliet";

        authenticateAs(rolesToAuthenticate);
        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, null);
        userService.createUser(userDTO);
    }

    @Test
    public void createUser_AsAnonymous() {
        List<String> rolesToAuthenticate = Arrays.asList(ROLE_ANONYMOUS);

        String email = "l.lawliet@deathnote.com";
        String password = "LLawliet";
        String firstName = "L";
        String lastName = "Lawliet";
        List<String> expectedRoles = Arrays.asList(
                RoleType.ROLE_USER.toString());

        authenticateAs(rolesToAuthenticate);
        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, null);
        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(expectedRoles, createdUser.getRoles());
    }

    @Test
    public void createUser_AsAnonymousWithInvalidRoles() {
        List<String> rolesToAuthenticate = Arrays.asList(ROLE_ANONYMOUS);

        String email = "misa.amane@deathnote.com";
        String password = "MisaAmane";
        String firstName = "Misa";
        String lastName = "Amane";
        List<String> roles = Arrays.asList(
                RoleType.ROLE_ADMIN.toString(),
                RoleType.ROLE_USER_CREATOR.toString(),
                RoleType.ROLE_USER_UPDATER.toString(),
                RoleType.ROLE_USER.toString());
        List<String> expectedRoles = Arrays.asList(
                RoleType.ROLE_USER.toString());

        authenticateAs(rolesToAuthenticate);
        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, roles);
        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(expectedRoles, createdUser.getRoles());
        assertNotEquals(roles, createdUser.getRoles());
    }

    @Test
    public void createUser_AsAdmin() {
        List<String> rolesToAuthenticate = Arrays.asList(
                RoleType.ROLE_ADMIN.toString());

        String email = "nate.river@deathnote.com";
        String password = "NateRiver";
        String firstName = "Nate";
        String lastName = "River";
        List<String> roles = Arrays.asList(
                RoleType.ROLE_ADMIN.toString(),
                RoleType.ROLE_USER_CREATOR.toString(),
                RoleType.ROLE_USER_UPDATER.toString(),
                RoleType.ROLE_USER.toString());

        authenticateAs(rolesToAuthenticate);
        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, roles);
        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(roles, createdUser.getRoles());
    }

    @Test
    public void createUser_AsUserCreator() {
        List<String> rolesToAuthenticate = Arrays.asList(
                RoleType.ROLE_USER_CREATOR.toString());

        String email = "kilua.zoldyck@hunterxhunter.com";
        String password = "KiluaZoldyck";
        String firstName = "Kilua";
        String lastName = "Zoldyck";
        List<String> roles = Arrays.asList(
                RoleType.ROLE_ADMIN.toString(),
                RoleType.ROLE_USER_CREATOR.toString(),
                RoleType.ROLE_USER_UPDATER.toString(),
                RoleType.ROLE_USER.toString());

        authenticateAs(rolesToAuthenticate);
        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, roles);
        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(roles, createdUser.getRoles());
    }

    @Test
    public void updateUser() {
        Long id = 1L;
        String email = "son.goku@dragonball.com";
        String password = "SonGoku";
        String firstName = "Son";
        String lastName = "Goku";

        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, null);
        UserDTO updatedUser = userService.updateUser(id, objectMapper.convertValue(userDTO, Map.class));

        assertNotNull(updatedUser);
        assertEquals(email, updatedUser.getEmail());
        assertEquals(firstName, updatedUser.getFirstName());
        assertEquals(lastName, updatedUser.getLastName());
    }

    private void authenticateAs(List<String> roleTypes) {
        Authentication authentication;
        List<GrantedAuthority> grantedAuthorities = roleTypes.stream()
                .map(roleType -> new SimpleGrantedAuthority(roleType))
                .collect(Collectors.toList());

        if (roleTypes.contains(ROLE_ANONYMOUS)) {
            authentication = new AnonymousAuthenticationToken("123", new Object(), grantedAuthorities);
        } else {
            User user = new User("user", "user", true, true, true, true, grantedAuthorities);
            authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}