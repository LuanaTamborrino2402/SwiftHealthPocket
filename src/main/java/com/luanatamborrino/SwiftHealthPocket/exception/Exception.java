package com.luanatamborrino.SwiftHealthPocket.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Model per le eccezioni personalizzate, include messaggio e status.
 */
@Getter
@AllArgsConstructor
public class Exception {

    private String message;

    private HttpStatus status;
}
