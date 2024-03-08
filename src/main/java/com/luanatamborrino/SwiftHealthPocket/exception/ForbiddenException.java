package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe per la gestione delle eccezioni con status code 403.
 * Estende RuntimeException, superclasse delle eccezioni
 * che possono essere lanciate durante il l'esecuzione della Java Virtual Machine.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message){

        super(message);
    }
}
