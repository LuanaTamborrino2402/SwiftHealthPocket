package com.luanatamborrino.SwiftHealthPocket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller per la gestione delle eccezioni.
 */
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * Handler delle eccezioni con status 400. Indica che la richiesta inviata dal client è errata
     * o non può essere elaborata dal server a causa di sintassi errate.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al cliente con messaggio e status.
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Exception> handleBadRequestException(BadRequestException e){

        Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(customException,HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler delle eccezioni con status 403. Indica che il client non ha i permessi necessari.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al cliente con messaggio e status.
     */
    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<Exception> handleForbiddenException(ForbiddenException e){

        Exception customException = new Exception(e.getMessage(), HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(customException,HttpStatus.FORBIDDEN);
    }

    /**
     * Handler delle eccezioni con status 403. Indica che la risorsa richiesta non è stata trovata sul server.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al cliente con messaggio e status.
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Exception> handleNotFoundException(NotFoundException e){

        Exception customException = new Exception(e.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customException,HttpStatus.NOT_FOUND);
    }

    /**
     * Handler delle eccezioni con status 409. Indica che il server non può completare la richiesta
     * a causa di un conflitto con lo stato attuale delle risorse sul server.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al cliente con messaggio e status.
     */
    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Exception> handleConflictException(ConflictException e){

        Exception customException = new Exception(e.getMessage(), HttpStatus.CONFLICT);

        return new ResponseEntity<>(customException,HttpStatus.CONFLICT);
    }

    /**
     * Handler delle eccezioni con status 500. Indica un errore generico sul lato server che impedisce di elaborare la richiesta.
     * @param e Messaggio dell'eccezione.
     * @return Risposta di errore al cliente con messaggio e status.
     */
    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Exception> handleInternalServerErrorException(InternalServerErrorException e){

        Exception customException = new Exception(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(customException,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
