package com.luanatamborrino.SwiftHealthPocket.observer.publisher;

import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.Subscriber;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler del pattern Observer. Questa classe fornisce metodi fondamentali
 * come subscribe, unsubscribe e notify per coordinare e gestire i publishers.
 */
@Component
public class Publisher {

    //HashMap per tutte le implementazioni dei listeners.
    private Map<String, Subscriber> listeners = new HashMap<>();

    /**
     * Metodo che registra un subscriber per un tipo specifico di evento.
     * @param eventType Tipo dell'evento usato come chiave della mappa.
     * @param subscriber Implementazione dell'interfaccia del subscriber.
     */
    public void subscribe(String eventType, Subscriber subscriber) {

        listeners.put(eventType, subscriber);
    }

    /**
     * Metodo per rimuovere tutti i subscriber associati a un tipo specifico di evento.
     * @param eventType Tipo dell'evento utilizzato per chiave della mappa.
     */
    public void unsubscribe(String eventType) {

        listeners.remove(eventType);
    }

    /**
     * Metodo che notifica i subscriber di un dato cambiamento.
     * @param eventType Tipo dell'evento utilizzato per chiave della mappa.
     * @param nome Nome dell'utente interessato dall'evento.
     * @param cognome Cognome dell'utente interessato dall'evento.
     * @param tipoPrestazione Il tipo di prestazione che viene effettuata.
     * @param esito Esito inerente al tipo di prestazione effettuata.
     * @param emailDestinatario Email del destinatario a cui arriva la notifica.
     */
    public void notify(String eventType, String nome, String cognome, String tipoPrestazione, String esito, String emailDestinatario) {

        listeners.get(eventType).update(nome, cognome, tipoPrestazione, esito, emailDestinatario);
    }
}
