package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe dedicata alla gestione delle eccezioni con status code 404.
 * Estende RuntimeException, superclasse per le eccezioni che possono essere lanciate
 * durante l'esecuzione della Java Virtual Machine.
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){

        super(message);
    }
}
