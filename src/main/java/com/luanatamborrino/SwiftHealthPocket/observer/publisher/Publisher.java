package com.luanatamborrino.SwiftHealthPocket.observer.publisher;

import com.luanatamborrino.SwiftHealthPocket.observer.subscriber.Subscriber;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Publisher del pattern Observer. La classe espone dei metodi per
 * entrambi i pattern: subscribe, unsubscribe e notify per la gestione dei listeners dell'Observer;
 */
@Component
public class Publisher {

    private Map<String, Subscriber> listeners = new HashMap<>();

    /**
     * Metodo che permette di iscrivere un listener al publisher.
     * @param eventType Tipo dell'evento usato come chiave della mappa.
     * @param subscriber Implementazione dell'interfaccia del subscriber.
     */
    public void subscribe(String eventType, Subscriber subscriber){

        listeners.put(eventType, subscriber);
    }

    /**
     * Metodo per annullare l'iscrizione di un listener al publisher.
     * @param eventType Tipo dell'evento utilizzato per chiave della mappa.
     */
    public void unsubscribe(String eventType){

        listeners.remove(eventType);
    }

    /**
     * Metodo che notifica i listeners di un dato cambiamento.
     * @param eventType Tipo dell'evento utilizzato per chiave della mappa.
     * @param nome Nome delpaziente che ha effettuato la prestazione.
     * @param cognome Cognome del paziente che ha effettuato la prestazione.
     * @param tipoPrestazione Il tipo di prestazione che viene effettuata.
     * @param esito Esito inerente al tipo di prestazione effettuata.
     * @param emailDestinatario Email del destinatario a cui arriva la notifica.
     */
    public void notify(String eventType, String nome, String cognome, String tipoPrestazione, String esito, String emailDestinatario){

        listeners.get(eventType).update(nome, cognome, tipoPrestazione, esito, emailDestinatario);
    }
}
