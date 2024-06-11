package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import com.luanatamborrino.SwiftHealthPocket.strategy.handler.CercaPrestazioneHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class PrestazioneControllerTest {

    @Mock
    PrestazioneService prestazioneService;
    @Mock
    CercaPrestazioneHandler cercaPrestazioneHandler;
    @InjectMocks
    PrestazioneController prestazioneController;

    @Test
    void prenota() {
        assertAll(() -> prestazioneController.prenota(null));
    }

    @Test
    void getAllByPaziente() {
        assertAll(() -> prestazioneController.getAllByPaziente("1"));
    }

    @Test
    void eliminaPrenotazione() {
        assertAll(() -> prestazioneController.eliminaPrenotazione("1"));
    }

    @Test
    void getAllPrenotazioni() {
        assertAll(() -> prestazioneController.getAllPrenotazioni("1"));
    }

    @Test
    void presaInCarico() {
        assertAll(() -> prestazioneController.presaInCarico(null));
    }

    @Test
    void esito() {
        assertAll(() -> prestazioneController.esito(null, "1"));
    }

    @Test
    void getPrenotazioniByPaziente() {
        assertAll(() -> prestazioneController.getPrenotazioniByPaziente("1"));
    }

    @Test
    void getPrenotazioniByInfermiere() {
        assertAll(() -> prestazioneController.getPrenotazioniByInfermiere("1"));
    }

    @Test
    void storicoPrestazioniByPaziente() {
        assertAll(() -> prestazioneController.storicoPrestazioniByPaziente("1"));
    }

    @Test
    void storicoPrestazioni() {
        assertAll(() -> prestazioneController.storicoPrestazioni());
    }

    @Test
    void cercaPrestazioni() {
        assertAll(() -> prestazioneController.cercaPrestazioni("1"));
    }
}
