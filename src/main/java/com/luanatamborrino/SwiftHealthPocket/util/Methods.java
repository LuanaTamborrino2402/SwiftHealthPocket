package com.luanatamborrino.SwiftHealthPocket.util;

import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;

import java.util.List;

/**
 * Classe che implementa il pattern Singleton per fornire metodi di validazione comuni.
 * Questa classe è utilizzata per centralizzare le verifiche di validità sui dati in ingresso
 * nei vari servizi dell'applicazione.
 */
public class Methods {

    //Unica istanza statica della classe.
    private static Methods instance;

    /**
     * Metodo del pattern Singleton per prendere sempre la stessa istanza e non crearne una nuova.
     * @return L'unica istanza della classe.
     */
    public static Methods getInstance() {

        //Se non esiste ancora un'istanza, la crea.
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
     * @param data La lista di Integer da verificare.
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

/* TODO questa è la classe del pattern. Il pattern viene applicato all'interno di OGNI metodo di OGNI service per controllare
    la validità dei dati. Quindi, al posto di fare i soliti if "stringa".isBlank() || "stringa".isEmpty(), chiamiamo questo
     metodo tramite Methods.getInstance().
     */