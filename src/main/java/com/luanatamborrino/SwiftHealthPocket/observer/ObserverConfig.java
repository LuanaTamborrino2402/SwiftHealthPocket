package com.luanatamborrino.SwiftHealthPocket.observer;

import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.ControlloEsito;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.InfermiereDissociato;
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

    @Bean
    public void start(){

        publisher.subscribe("InfermiereDissociato", infermiereDissociato);
        publisher.subscribe("ControlloEsito", controlloEsito);
    }

    @PreDestroy
    public void stop(){

        publisher.unsubscribe("InfermiereDissociato");
        publisher.unsubscribe("ControlloEsito");
    }
}
