package com.drawsforall.user.management.business;

import com.drawsforall.user.management.persistence.entity.User;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        if ( userDTO.getLinks() != null ) {
            List<Link> list = buildLinksToUserDTO( user );
            if ( list != null ) {
                userDTO.getLinks().addAll( list );
            }
        }
        userDTO.setUserId( user.getId() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setRole( user.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toList()) );
        userDTO.setDisplay( user.getFirstName() + " " + user.getLastName() );

        return userDTO;
    }

    @Override
    public List<UserDTO> toUserDTO(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( toUserDTO( user ) );
        }

        return list;
    }

    @Override
    public User fromUserDTO(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getUserId() );
        user.setEmail( userDTO.getEmail() );
        user.setPassword( userDTO.getPassword() );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );

        return user;
    }
}
