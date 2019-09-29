package com.drawsforall.user.management.business.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User " + id + " not found");
    }
}
