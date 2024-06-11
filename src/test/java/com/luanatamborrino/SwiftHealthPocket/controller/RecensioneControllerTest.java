package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.service.RecensioneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class RecensioneControllerTest {

    @Mock
    RecensioneService recensioneService;
    @InjectMocks
    RecensioneController recensioneController;

    @Test
    void salva() {
        assertAll(() -> recensioneController.salva("1", null));
    }

    @Test
    void getAllByPaziente() {
        assertAll(() -> recensioneController.getAllByPaziente("1"));
    }

    @Test
    void getByPrestazioneId() {
        assertAll(() -> recensioneController.getByPrestazioneId("1"));
    }
}
