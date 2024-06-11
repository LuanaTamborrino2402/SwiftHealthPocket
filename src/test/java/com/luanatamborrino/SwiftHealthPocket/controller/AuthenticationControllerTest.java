package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.response.LoginResponse;
import com.luanatamborrino.SwiftHealthPocket.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    AuthenticationService authenticationService;
    @InjectMocks
    AuthenticationController authenticationController;

    @Test
    void register() {
        assertAll(() -> authenticationController.register(null));
    }

    @Test
    void authenticate() {
        when(authenticationService.authenticate(any())).thenReturn(new LoginResponse("message", "jwt"));
        when(authenticationService.putJwtInHttpHeaders(any())).thenReturn(new HttpHeaders());
        assertAll(() -> authenticationController.authenticate(null));
    }
}
