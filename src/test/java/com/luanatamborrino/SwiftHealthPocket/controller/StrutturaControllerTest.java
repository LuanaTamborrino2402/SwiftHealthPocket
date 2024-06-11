package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.service.StrutturaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class StrutturaControllerTest {

    @Mock
    StrutturaService strutturaService;
    @InjectMocks
    StrutturaController strutturaController;

    @Test
    void creaStruttura() {
        assertAll(() -> strutturaController.creaStruttura(null));
    }

    @Test
    void getStrutturaData() {
        assertAll(() -> strutturaController.getStrutturaData("1"));
    }

    @Test
    void getAllStrutture() {
        assertAll(() -> strutturaController.getAllStrutture());
    }

    @Test
    void updateStruttura() {
        assertAll(() -> strutturaController.updateStruttura("1", null));
    }

    @Test
    void deleteStrutturaById() {
        assertAll(() -> strutturaController.deleteStrutturaById("1"));
    }

    @Test
    void associaInfermiere() {
        assertAll(() -> strutturaController.associaInfermiere(null));
    }

    @Test
    void dissociaInfermiere() {
        assertAll(() -> strutturaController.dissociaInfermiere(null));
    }
}
