package com.luanatamborrino.SwiftHealthPocket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Exception> handleBadRequestException(BadRequestException e){
       Exception customException = new Exception(e.getMessage(), HttpStatus.BAD_REQUEST);


       return new ResponseEntity<>(customException,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<Exception> handleForbiddenException(ForbiddenException e){
        Exception customException = new Exception(e.getMessage(), HttpStatus.FORBIDDEN);


        return new ResponseEntity<>(customException,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Exception> handleNotFoundException(NotFoundException e){
        Exception customException = new Exception(e.getMessage(), HttpStatus.NOT_FOUND);


        return new ResponseEntity<>(customException,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Exception> handleConflictException(ConflictException e){
        Exception customException = new Exception(e.getMessage(), HttpStatus.CONFLICT);


        return new ResponseEntity<>(customException,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Exception> handleInternalServerErrorException(InternalServerErrorException e){
        Exception customException = new Exception(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);


        return new ResponseEntity<>(customException,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
