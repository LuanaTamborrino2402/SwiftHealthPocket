package com.luanatamborrino.SwiftHealthPocket.util;

import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;

import java.util.List;

/**
 * Classe che implementa il pattern Singleton per fornire metodi di validazione condivisi.
 * Centralizza le verifiche di integrità dei dati in ingresso nei vari servizi dell'applicazione,
 * garantendo coerenza e riusabilità delle validazioni.
 */
public class Methods {

    //Unica istanza statica della classe.
    private static Methods instance;

    /**
     * Metodo del pattern Singleton per prendere sempre la stessa istanza e non crearne una nuova.
     * @return L'unica istanza della classe.
     */
    public static Methods getInstance() {

        //Crea l'istanza se non esiste ancora.
        if(instance == null) {
            instance = new Methods();
        }

        return instance;
    }

    /**
     * Verifica che le stringhe in una lista non siano vuote o null.
     * @param data La lista di stringhe da verificare.
     */
    public void checkStringData(List<String> data) {
        for(String s : data) {
            if(s.isBlank() || s.isEmpty()) {
                throw new BadRequestException("Inserire tutti i campi");
            }
        }
    }

    /**
     * Verifica l'integrità dei dati numerici (non siano nulli).
     * @param data La lista di interi da verificare.
     */
    public void checkIntegerData(List<Integer> data) {
        for(Integer i : data) {
            if(i == null) {
                throw new BadRequestException("Inserire tutti i campi");
            }
        }
    }

    /**
     * Verifica che gli ID in una lista siano validi (non nulli e maggiori di zero).
     * @param data La lista di Long rappresentante gli id da verificare.
     */
    public void checkIds(List<Long> data) {
        for(Long l : data) {
            if(l == null || l <=0) {
                throw new BadRequestException("Id non valido");
            }
        }
    }
}
