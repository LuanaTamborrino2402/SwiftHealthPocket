package com.luanatamborrino.SwiftHealthPocket.observer;

import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.ControlloEsito;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.InfermiereDissociato;
import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.RichiestaCambioStruttura;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe di configurazione per l'Observer che viene attivata all'avvio dell'applicazione per effetturare le subscirbe.
 * L'annotazione "@Configuration" assicura che le operazioni definite vengano eseguite non appena il server è operativo.
 */
@Configuration
@RequiredArgsConstructor
public class ObserverConfig {

    private final Publisher publisher;

    private final ControlloEsito controlloEsito;
    private final InfermiereDissociato infermiereDissociato;
    private final RichiestaCambioStruttura richiestaCambioStruttura;

    /**
     * Metodo che inizializza l'Observer facendo le subscirbe per ogni implementazione del listener.
     */
    @Bean
    public void start() {

        publisher.subscribe("InfermiereDissociato", infermiereDissociato);
        publisher.subscribe("ControlloEsito", controlloEsito);
        publisher.subscribe("RichiestaCambioStruttura", richiestaCambioStruttura);
    }

    /**
     * L'annotazione @PreDestroy assicura che questo metodo venga invocato prima della chiusura dell'applicazioneper effettuare tutti gli unsubscribe.
     */
    @PreDestroy
    public void stop() {

        publisher.unsubscribe("InfermiereDissociato");
        publisher.unsubscribe("ControlloEsito");
        publisher.unsubscribe("RichiestaCambioStruttura");
    }
}
