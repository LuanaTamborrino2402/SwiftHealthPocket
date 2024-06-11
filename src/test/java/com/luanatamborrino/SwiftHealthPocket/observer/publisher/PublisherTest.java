package com.luanatamborrino.SwiftHealthPocket.observer.publisher;

import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.ControlloEsito;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.Subscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublisherTest {

    @Mock
    Map<String, Subscriber> listeners;
    @Mock
    ControlloEsito controlloEsito;
    @InjectMocks
    Publisher publisher;

    @Test
    void subscribe() {
        assertAll(() -> publisher.subscribe("eventType", controlloEsito));
    }

    @Test
    void unsubscribe() {
        assertAll(() -> publisher.unsubscribe("eventType"));
    }

    @Test
    void notifySuccessful() {
        when(listeners.get("eventType")).thenReturn(controlloEsito);
        assertAll(() -> publisher.notify("eventType", "nome", "cognome", "tipoPrestazione", "esito", "emailDestinatario"));
    }
}
