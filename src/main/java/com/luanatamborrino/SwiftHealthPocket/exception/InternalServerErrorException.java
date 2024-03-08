package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe per la gestione delle eccezioni con status code 500.
 * Estende RuntimeException, superclasse delle eccezioni
 * che possono essere lanciate durante il l'esecuzione della Java Virtual Machine.
 */
public class InternalServerErrorException extends RuntimeException{

    public InternalServerErrorException(String message){

        super(message);
    }
}
