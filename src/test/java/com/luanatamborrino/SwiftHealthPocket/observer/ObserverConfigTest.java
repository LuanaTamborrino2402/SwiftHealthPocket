package com.luanatamborrino.SwiftHealthPocket.observer;

import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.ControlloEsito;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.InfermiereDissociato;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.RichiestaCambioStruttura;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ObserverConfigTest {

    @Mock
    Publisher publisher;
    @Mock
    ControlloEsito controlloEsito;
    @Mock
    InfermiereDissociato infermiereDissociato;
    @Mock
    RichiestaCambioStruttura richiestaCambioStruttura;
    @InjectMocks
    ObserverConfig observerConfig;

    @Test
    void start() {
        observerConfig.start();
    }

    @Test
    void stop() {
        observerConfig.stop();
    }
}
