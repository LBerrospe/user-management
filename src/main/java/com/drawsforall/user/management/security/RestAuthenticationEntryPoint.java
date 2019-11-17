package com.drawsforall.user.management.security;

import com.drawsforall.user.management.web.rest.dto.APIErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;

    @Autowired
    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException ex) throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON.toString());
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(LocalDateTime.now(), UNAUTHORIZED, ex.getMessage());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

