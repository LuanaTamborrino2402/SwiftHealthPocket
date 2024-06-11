package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe dedicata alla gestione delle eccezioni con status code 400.
 * Estende RuntimeException, superclasse per le eccezioni che possono essere lanciate
 * durante l'esecuzione della Java Virtual Machine.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message){

        super(message);
    }
}
