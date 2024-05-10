package com.luanatamborrino.SwiftHealthPocket.util;

import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;

import java.util.List;

public class Methods {

    private static Methods instance;

    public static Methods getInstance() {

        if(instance == null) {
            instance = new Methods();
        }

        return instance;
    }

    public void checkStringData(List<String> data) {
        for(String s : data) {
            if(s.isBlank() || s.isEmpty()) {
                throw new BadRequestException("Inserire tutti i campi");
            }
        }
    }

    public void checkIntegerData(List<Integer> data) {
        for(Integer i : data) {
            if(i == null) {
                throw new BadRequestException("Inserire tutti i campi");
            }
        }
    }

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
     Prima di pushare questo pattern, ti consiglio di controllare OGNI singolo metodo su postman per vedere se
     questi controlli funzionano correttamente. */