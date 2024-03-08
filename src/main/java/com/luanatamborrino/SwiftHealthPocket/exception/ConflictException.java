package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe per la gestione delle eccezioni con status code 409.
 * Estende RuntimeException, superclasse delle eccezioni
 * che possono essere lanciate durante il l'esecuzione della Java Virtual Machine.
 */
public class ConflictException extends RuntimeException{

    public ConflictException(String message){

        super(message);
    }
}
