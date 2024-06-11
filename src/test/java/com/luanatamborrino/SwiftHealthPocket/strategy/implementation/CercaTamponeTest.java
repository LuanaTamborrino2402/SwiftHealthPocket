package com.luanatamborrino.SwiftHealthPocket.strategy.implementation;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CercaTamponeTest {

    @Mock
    PrestazioneService prestazioneService;
    @InjectMocks
    CercaTampone cercaTampone;

    @Test
    void cercaPrestazioni() {
        when(prestazioneService.cercaTampone()).thenReturn(List.of(new Prestazione()));
        assertAll(() -> cercaTampone.cercaPrestazioni());
    }
}
