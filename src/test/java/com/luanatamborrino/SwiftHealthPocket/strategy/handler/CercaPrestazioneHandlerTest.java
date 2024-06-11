package com.luanatamborrino.SwiftHealthPocket.strategy.handler;

import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model._enum.EsitoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.model._enum.TipoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import com.luanatamborrino.SwiftHealthPocket.strategy.CercaPrestazioneStrategy;
import com.luanatamborrino.SwiftHealthPocket.strategy.implementation.CercaVaccino;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CercaPrestazioneHandlerTest {

    @Mock
    PrestazioneService prestazioneService;
    @Mock
    CercaPrestazioneStrategy strategy;
    @InjectMocks
    CercaPrestazioneHandler cercaPrestazioneHandler;

    @Test
    void scegliStrategySuccessfulTampone() {
        assertAll(() -> cercaPrestazioneHandler.scegliStrategy("TAMPONE"));
    }

    @Test
    void scegliStrategySuccessfulVaccino() {
        assertAll(() -> cercaPrestazioneHandler.scegliStrategy("VACCINO"));
    }

    @Test
    void scegliStrategyThrowsTipoPrestazioneNonValido() {
        assertThrows(BadRequestException.class, () -> cercaPrestazioneHandler.scegliStrategy("AAA"));
    }

    @Test
    void eseguiStrategySuccessful() {
        when(strategy.cercaPrestazioni()).thenReturn(List.of(
                Prestazione.builder()
                        .idPrestazione(1L)
                        .tipoPrestazione(TipoPrestazione.TAMPONE)
                        .esito(EsitoPrestazione.TAMPONE_NEGATIVO)
                        .dataInizio(LocalDateTime.now().plusDays(4))
                        .dataFine(LocalDateTime.now().plusDays(5))
                        .build()
        ));
        assertAll(() -> cercaPrestazioneHandler.eseguiStrategy(strategy));
    }
}
