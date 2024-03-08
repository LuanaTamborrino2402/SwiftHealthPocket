package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe per la gestione delle eccezioni con status code 404.
 * Estende RuntimeException, superclasse delle eccezioni
 * che possono essere lanciate durante il l'esecuzione della Java Virtual Machine.
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){

        super(message);
    }
}
