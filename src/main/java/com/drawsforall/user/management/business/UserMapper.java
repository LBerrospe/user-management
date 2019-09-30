package com.drawsforall.user.management.business;

import com.drawsforall.user.management.persistence.entity.User;
import com.drawsforall.user.management.web.rest.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User users);

    List<UserDTO> toUserDTO(List<User> users);

    User fromUserDTO(UserDTO userDTO);
}
