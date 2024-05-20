package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

/**
 * Interfaccia che rappresenta il subscriber del pattern Observer. Contiene la firma del
 * metodo update chiamato per gestire gli aggiornamenti relativi agli eventi a cui sono sottoscritti.
 */
public interface Subscriber {

   /**
    * Metodo chiamato dal publisher per notificare un evento ai subscriber.
    * @param nome Nome della persona coinvolta nell'evento.
    * @param cognome Cognome della persona coinvolta nell'evento.
    * @param tipoPrestazione Il tipo di prestazione correlata all'evento.
    * @param esito Esito dell'evento.
    * @param emailDestinatario Indirizzo email del destinatario a cui inviare la notifica.
    */
   void update(String nome, String cognome, String tipoPrestazione, String esito, String emailDestinatario);
}
