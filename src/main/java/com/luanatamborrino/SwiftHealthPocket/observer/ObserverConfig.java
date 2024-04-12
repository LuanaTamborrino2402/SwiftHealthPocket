package com.luanatamborrino.SwiftHealthPocket.observer;

import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.ControlloEsito;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.InfermiereDissociato;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.RichiestaCambioStruttura;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ObserverConfig {

    private final Publisher publisher;

    private final ControlloEsito controlloEsito;
    private final InfermiereDissociato infermiereDissociato;
    private final RichiestaCambioStruttura richiestaCambioStruttura;

    @Bean
    public void start(){

        publisher.subscribe("InfermiereDissociato", infermiereDissociato);
        publisher.subscribe("ControlloEsito", controlloEsito);
        publisher.subscribe("RichiestaCambioStruttura", richiestaCambioStruttura);

    }

    @PreDestroy
    public void stop(){

        publisher.unsubscribe("InfermiereDissociato");
        publisher.unsubscribe("ControlloEsito");
        publisher.unsubscribe("RichiestaCambioStruttura");

    }
}
