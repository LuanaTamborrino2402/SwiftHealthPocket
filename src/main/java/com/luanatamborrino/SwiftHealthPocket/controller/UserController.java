package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.UpdateUserDataRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.UserResponse;
import com.luanatamborrino.SwiftHealthPocket.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per l'utente.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/utente")
public class UserController {

    private final UtenteService userService;

    /**
     * Metodo che restituisce i dati di un utente identificato dall'id fornito.
     * @param id Id dell'utente.
     * @return DTO con tutti i dati dell'utente richiesto.
     */
    @GetMapping("/getUserData/{id}")
    public ResponseEntity<UserResponse> getUserData(@PathVariable String id){

        long userId = Long.parseLong(id);

        final UserResponse response = userService.getUserData(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo per eliminare un utente dal database.
     * @param id Id dell'utente.
     * @return Messaggio di risposta al client.
     */
    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<MessageResponse> deleteUserById(@PathVariable String id){

        long userId = Long.parseLong(id);

        userService.deleteUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Utente eliminato."));
    }

    /**
     * Metodo per eliminare un utente dal database tramite l'indirizzo email.
     * @param email L'indirizzo email dell'utente da eliminare.
     * @return Messaggio di risposta al client.
     */
    @DeleteMapping("/deleteUserByEmail/{email}")
    public ResponseEntity<MessageResponse> deleteUserByEmail(@PathVariable String email){

        userService.deleteUserByEmail(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Utente eliminato."));

    }

    /**
     * Metodo per prendere tutti gli utenti presenti sul database.
     * @return Lista di DTO con i dati di ogni utente.
     */
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers(){

        final List<UserResponse> response = userService.getAllUsers();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    /**
     * Metodo per ottenere tutti gli utenti di un determinato ruolo.
     * @param ruolo Ruolo richiesto.
     * @return Lista di DTO con i dati degli utenti con il ruolo specificato.
     */
    @GetMapping("/getAllUsersByRole/{ruolo}")
    public ResponseEntity<List<UserResponse>> getAllUsersByRole(@PathVariable String ruolo){

        final List<UserResponse> response = userService.getAllUsersByRole(ruolo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    /**
     * Metodo che permette di modificare i dati di un utente.
     * @param id Id dell'utente da modificare.
     * @param request DTO con tutti i nuovi dati da sovrascrivere.
     * @return DTO con i dati dell'utente modificati.
     */
    @PutMapping("/updateUserData/{id}")
    public ResponseEntity<UserResponse> updateUserData(@PathVariable String id, @RequestBody UpdateUserDataRequest request){

        long userId = Long.parseLong(id);

        final UserResponse response = userService.updateUserData(userId,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    /**
     * Metodo per verificare la disponibilità di un infermiere tramite l'id utente.
     * @param id Id dell'infermiere.
     * @return Messaggio di risposta al client.
     */
    @GetMapping("/getDisponibilitaInfermiere/{id}")
    public ResponseEntity<MessageResponse> getDisponibilitaInfermiere(@PathVariable String id){

        long userId = Long.parseLong(id);

        userService.getDisponibilitaInfermiere(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Utente disponibile."));

    }

    @GetMapping("/richiestaCambioStruttura/{id}")
    public ResponseEntity<MessageResponse> richiestaCambioStruttura(@PathVariable String id){

        long userId = Long.parseLong(id);

        userService.richiestaCambioStruttura(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Richiesta effettuata."));

    }
}
