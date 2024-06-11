package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.AuthenticationRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.RegisterRequest;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.ConflictException;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import com.luanatamborrino.SwiftHealthPocket.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void registerThrowsRegistrazioneFallita() {
        RegisterRequest request = new RegisterRequest("nome", "cognome", "email", "password", "PAZIENTE");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        assertThrows(InternalServerErrorException.class, () -> authenticationService.register(request));
    }

    @Test
    void registerThrowsEmailGiaRegistrata() {
        RegisterRequest request = new RegisterRequest("nome", "cognome", "email", "password", "INFERMIERE");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new Utente()));
        assertThrows(ConflictException.class, () -> authenticationService.register(request));
    }

    @Test
    void registerThrowsRuoloNonValido() {
        RegisterRequest request = new RegisterRequest("nome", "cognome", "email", "password", "ruolo");
        assertThrows(BadRequestException.class, () -> authenticationService.register(request));
    }

    @Test
    void authenticateSuccessful() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new Utente()));
        assertAll(() -> authenticationService.authenticate(new AuthenticationRequest("email", "password")));
    }

    @Test
    void putJwtInHttpHeaders() {
        assertAll(() -> authenticationService.putJwtInHttpHeaders("jwt"));
    }
}
