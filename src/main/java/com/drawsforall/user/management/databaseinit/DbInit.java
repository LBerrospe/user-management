package com.drawsforall.user.management.databaseinit;

import com.drawsforall.user.management.persistence.RoleRepository;
import com.drawsforall.user.management.persistence.UserRepository;
import com.drawsforall.user.management.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class DbInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        User user= new User();
        user.setEmail("goku@gmail.com");
        user.setPassword(passwordEncoder.encode("dragonball"));
        user.setFirstName("Goku");
        user.setLastName("Akira");
        user.setRoles(roleRepository.find(Collections.singletonList("ADMIN")));
        userRepository.save(user);
        log.debug("User created {}", user);

    }
}
