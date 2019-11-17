package com.drawsforall.user.management.security;

import com.drawsforall.user.management.web.rest.dto.APIErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper;

    @Autowired
    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException ex) throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON.toString());
        httpServletResponse.setStatus(SC_FORBIDDEN);
        APIErrorResponseDTO errorResponse = new APIErrorResponseDTO(LocalDateTime.now(), FORBIDDEN, ex.getMessage());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}