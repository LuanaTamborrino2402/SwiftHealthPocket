package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.service.UtenteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UtenteService userService;
    @InjectMocks
    UserController userController;

    @Test
    void getUserData() {
        assertAll(() -> userController.getUserData("1"));
    }

    @Test
    void deleteUserById() {
        assertAll(() -> userController.deleteUserById("1"));
    }

    @Test
    void deleteUserByEmail() {
        assertAll(() -> userController.deleteUserByEmail("email"));
    }

    @Test
    void getAllUsers() {
        assertAll(() -> userController.getAllUsers());
    }

    @Test
    void getAllUsersByRole() {
        assertAll(() -> userController.getAllUsersByRole("role"));
    }

    @Test
    void updateUserData() {
        assertAll(() -> userController.updateUserData("1", null));
    }

    @Test
    void getDisponibilitaInfermiere() {
        assertAll(() -> userController.getDisponibilitaInfermiere("1"));
    }

    @Test
    void richiestaCambioStruttura() {
        assertAll(() -> userController.richiestaCambioStruttura("1"));
    }

    @Test
    void cambioForzatoStruttura() {
        assertAll(() -> userController.cambioForzatoStruttura(null));
    }
}
