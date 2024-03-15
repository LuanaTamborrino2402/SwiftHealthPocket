package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.response.UserResponse;
import com.luanatamborrino.SwiftHealthPocket.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/utente")
public class UserController {

    private final UtenteService userService;

    @GetMapping("/getUserData/{id}")
    public ResponseEntity<UserResponse> getUserData(@PathVariable String id){

        long userId = Long.parseLong(id);

        final UserResponse response = userService.getUserData(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
