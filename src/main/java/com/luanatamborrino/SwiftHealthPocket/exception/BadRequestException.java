package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe per la gestione delle eccezioni con status code 400.
 * Estende RuntimeException, superclasse delle eccezioni
 * che possono essere lanciate durante l'esecuzione della Java Virtual Machine.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message){

        super(message);
    }
}
