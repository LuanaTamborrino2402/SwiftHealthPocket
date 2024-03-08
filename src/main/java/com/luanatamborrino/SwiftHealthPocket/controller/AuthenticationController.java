package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.AuthenticationRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.RegisterRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.LoginResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {

        authenticationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("Registrazione completata!"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody AuthenticationRequest request){

        final LoginResponse response = authenticationService.authenticate(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(authenticationService.putJwtInHttpHeaders(response.getJwt()))
                .body(response);
    }
}
