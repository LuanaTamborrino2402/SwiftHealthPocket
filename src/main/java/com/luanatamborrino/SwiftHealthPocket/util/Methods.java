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
                throw new BadRequestException("Valore non valido");
            }
        }
    }

    public void checkIntegerData(List<Integer> data) {
        for(Integer i : data) {
            if(i == null) {
                throw new BadRequestException("Valore non valido");
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
