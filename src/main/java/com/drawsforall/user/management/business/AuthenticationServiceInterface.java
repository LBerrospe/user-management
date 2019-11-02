package com.drawsforall.user.management.business;

import org.springframework.security.core.Authentication;

public interface AuthenticationServiceInterface {

    Authentication getAuthentication();
}
