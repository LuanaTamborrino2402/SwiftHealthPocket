package com.luanatamborrino.SwiftHealthPocket.exception;

/**
 * Classe dedicata alla gestione delle eccezioni con status code 500.
 * Estende RuntimeException, superclasse per le eccezioni che possono essere lanciate
 * durante l'esecuzione della Java Virtual Machine.
 */
public class InternalServerErrorException extends RuntimeException{

    public InternalServerErrorException(String message){

        super(message);
    }
}
