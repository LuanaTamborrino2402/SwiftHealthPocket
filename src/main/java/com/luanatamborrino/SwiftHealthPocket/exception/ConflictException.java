package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe dedicata alla gestione delle eccezioni con status code 409.
 * Estende RuntimeException, superclasse per le eccezioni che possono essere lanciate
 * durante l'esecuzione della Java Virtual Machine.
 */
public class ConflictException extends RuntimeException{

    public ConflictException(String message){

        super(message);
    }
}
