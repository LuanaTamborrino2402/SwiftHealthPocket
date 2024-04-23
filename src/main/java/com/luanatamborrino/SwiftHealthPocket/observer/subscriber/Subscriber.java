package com.luanatamborrino.SwiftHealthPocket.observer.subscriber;

/**
 *
 */
public interface Subscriber {

   void update(String nome, String cognome, String tipoPrestazione, String esito, String emailDestinatario);
}
