package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.UpdateUserDataRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.UserResponse;
import com.luanatamborrino.SwiftHealthPocket.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<MessageResponse> deleteUserById(@PathVariable String id){

        long userId = Long.parseLong(id);

        userService.deleteUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Utente eliminato."));
    }

    @DeleteMapping("/deleteUserByEmail/{email}")
    public ResponseEntity<MessageResponse> deleteUserByEmail(@PathVariable String email){

        userService.deleteUserByEmail(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Utente eliminato."));

    }


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers(){

        final List<UserResponse> response = userService.getAllUsers();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @GetMapping("/getAllUsersByRole/{ruolo}")
    public ResponseEntity<List<UserResponse>> getAllUsersByRole(@PathVariable String ruolo){

        final List<UserResponse> response = userService.getAllUsersByRole(ruolo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @PutMapping("/updateUserData/{id}")
    public ResponseEntity<UserResponse> updateUserData(@PathVariable String id, @RequestBody UpdateUserDataRequest request){

        long userId = Long.parseLong(id);

        final UserResponse response = userService.updateUserData(userId,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }
}
