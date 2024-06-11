package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe dedicata alla gestione delle eccezioni con status code 403.
 * Estende RuntimeException, superclasse per le eccezioni che possono essere lanciate
 * durante l'esecuzione della Java Virtual Machine.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message){

        super(message);
    }
}
