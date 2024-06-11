package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

import com.luanatamborrino.SwiftHealthPocket.mailing.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class ControlloEsitoTest {

    @Mock
    EmailService emailService;
    @InjectMocks
    ControlloEsito controlloEsito;

    @Test
    void update() {
        assertAll(() -> controlloEsito.update("nome", "cognome", "tipoPrestazione", "esito", "emailDestinatario"));
    }
}
