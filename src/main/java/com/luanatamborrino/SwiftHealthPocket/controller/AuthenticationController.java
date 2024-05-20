package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.AuthenticationRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.RegisterRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.LoginResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller per l'autenticazione.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Metodo per la registrazione di un nuovo utente nel sistema.
     * Chiama il service che si occupa di salvare l'utente nel database.
     * @param request DTO con i dati per la registrazione.
     * @return Messaggio di avvenuta registrazione.
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {

        authenticationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("Registrazione completata!"));
    }

    /**
     * Metodo per la l'autenticazione di un utente. Chiama il service per verificare le credenziali dell'utente.
     * Se l'autenticazione ha successo, restituisce il token JWT nell'header della risposta HTTP
     * insieme ai dettagli dell'utente loggato.
     * @param request DTO con i dati per l'autenticazione.
     * @return DTO con l'utente autenticato e messaggio di avvenuta autenticazione.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody AuthenticationRequest request) {

        final LoginResponse response = authenticationService.authenticate(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(authenticationService.putJwtInHttpHeaders(response.getJwt()))
                .body(response);
    }
}
