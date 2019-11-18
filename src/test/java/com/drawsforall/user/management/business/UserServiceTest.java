package com.drawsforall.user.management.business;

import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
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
    public void setUp() throws Exception {
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

    @Test
    public void lookupUserById() {
        String by = "id";
        String value = "1";
        UserDTO fetchedUser = userService.lookupUser(by, value);

        assertNotNull(fetchedUser);
        assertEquals(new Long(value), fetchedUser.getUserId());
    }

    @Test
    public void lookupUserByEmail() {
        String by = "email";
        String value = "yagami.light@deathnote.com";
        UserDTO fetchedUser = userService.lookupUser(by, value);

        assertNotNull(fetchedUser);
        assertTrue(value.equalsIgnoreCase(fetchedUser.getEmail()));
    }

    @Ignore
    @Test
    public void createUser() {
        String email = "l.lawliet@deathnote.com";
        String password = "LLawliet";
        String firstName = "L";
        String lastName = "Lawliet";

        UserDTO userDTO = new UserDTO(null, email, password, firstName, lastName, null, null, null, null);
        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals(password, createdUser.getPassword());
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
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
}