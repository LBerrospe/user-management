package com.drawsforall.user.management.business;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "userId", source = "id"),
            @Mapping(target = "display", expression = "java(user.getFirstName() + \" \" + user.getLastName())"),
            @Mapping(target = "links", source = "user", qualifiedByName = "buildLinksToUserDTO")
    })
    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTO(List<User> users);

    @Mapping(target = "id", source = "userId")
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
        Link selfOrUpdateOrDelete = linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel("self/update/delete");
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
}
