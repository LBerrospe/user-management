package com.drawsforall.user.management.business;

import com.drawsforall.user.management.persistence.entity.Role;
import com.drawsforall.user.management.persistence.entity.User;
import com.drawsforall.user.management.web.rest.UserController;
import com.drawsforall.user.management.web.rest.dto.PagedUsersDTO;
import com.drawsforall.user.management.web.rest.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "userId", source = "id"),
            @Mapping(target = "display", expression = "java(user.getFirstName() + \" \" + user.getLastName())"),
            @Mapping(target = "links", source = "user", qualifiedByName = "buildLinksToUserDTO"),
            @Mapping(target = "roles", qualifiedByName = "setRolesToUserDTO"),
            @Mapping(target = "password", qualifiedByName = "setEncodedPassword")
    })
    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTO(List<User> users);

    @Mappings({
            @Mapping(target = "id", source = "userId"),
            @Mapping(target = "password", qualifiedByName = "setDecodedPassword"),
            @Mapping(target = "roles", ignore = true)
    })
    User fromUserDTO(UserDTO userDTO);

    default PagedUsersDTO toPagedUsersDTO(Page<User> pagedUsers) {
        Page<UserDTO> page = pagedUsers.map(this::toUserDTO);
        PagedUsersDTO pagedUsersDTO = PagedUsersDTO.builder()
                .elements(page.getContent())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .numberOfElements(page.getNumberOfElements())
                .number(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        pagedUsersDTO.add(buildLinksToPagedUsersDTO(pagedUsers.getNumber(), pagedUsers.getSize(), pagedUsers.hasNext(), pagedUsers.hasPrevious()));
        return pagedUsersDTO;
    }

    @Named("userDTOLinks")
    default List<Link> buildLinksToUserDTO(User user) {
        Link all = linkTo(methodOn(UserController.class).getUsers(null, null)).withRel("all");
        Link selfOrUpdateOrDelete = linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel("self");
        return Arrays.asList(all, selfOrUpdateOrDelete);
    }

    default List<Link> buildLinksToPagedUsersDTO(int number, int size, boolean hasNext, boolean hasPrevious) {
        List<Link> links = new ArrayList<>();

        Link selfRel = linkTo(methodOn(UserController.class).getUsers(number, size)).withSelfRel();
        links.add(selfRel);
        if (hasNext) {
            Link nextPage = linkTo(methodOn(UserController.class).getUsers(number + 1, size)).withRel("nextPage");
            links.add(nextPage);
        }

        if (hasPrevious) {
            Link previousPage = linkTo(methodOn(UserController.class).getUsers(number - 1, size)).withRel("previousPage");
            links.add(previousPage);
        }

        return links;
    }

    @Named("setRolesToUserDTO")
    default List<String> setRolesToUserDTO(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toList());
    }

    @Named("setEncodedPassword")
    default String setEncoderPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Named("setDecodedPassword")
    default String setDecodedPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
