package com.drawsforall.user.management.web.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Builder
@Getter
@ToString
public class PagedUsersDTO extends ResourceSupport {

    private List<UserDTO> elements;

    @Accessors(fluent = true)
    private boolean hasNext;

    @Accessors(fluent = true)
    private boolean hasPrevious;

    private int numberOfElements;

    private int number;

    private int size;

    private long totalElements;

    private int totalPages;
}
