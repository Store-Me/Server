package com.example.storeme.global.filter;

import com.example.storeme.global.common.code.BaseErrorCode;
import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.common.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("JwtAuthenticationEntryPoint에서 response 생성중, errorMsg: {}", request.getAttribute("ErrorMsg"));

        BaseErrorCode errorCode = (BaseErrorCode)request.getAttribute("exception");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, ResponseDto.onFailure(errorCode));
            os.flush();
        }
    }
}
