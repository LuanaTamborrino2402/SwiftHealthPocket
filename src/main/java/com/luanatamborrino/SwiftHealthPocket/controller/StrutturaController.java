package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.AssociaDissociaInfermiereRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.CreaModificaStrutturaRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.StrutturaResponse;
import com.luanatamborrino.SwiftHealthPocket.service.StrutturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la struttura.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/struttura")
public class StrutturaController {

    private final StrutturaService strutturaService;

    /**
     * Metodo
     * @param request
     * @return
     */
    @PostMapping("/creaStruttura")
    public ResponseEntity<MessageResponse> creaStruttura (@RequestBody CreaModificaStrutturaRequest request){

        strutturaService.creaStruttura(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Struttura registrata."));


    }

    /**
     * Metodo che restituisce i dati di una struttura identificata dall'id fornito.
     * @param id Id della struttura.
     * @return DTO con i tutti i dati della struttura richiesta.
     */
    @GetMapping("/getStrutturaData/{id}")
    public ResponseEntity<StrutturaResponse> getStrutturaData(@PathVariable String id){

        long strutturaId = Long.parseLong(id);

        final StrutturaResponse response = strutturaService.getStrutturaData(strutturaId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo per prendere tutte le strutture presenti sul database.
     * @return Lista di DTO con i dati di ogni struttura.
     */
    @GetMapping("/getAllStrutture")
    public ResponseEntity<List<StrutturaResponse>> getAllStrutture(){

        final List<StrutturaResponse> response = strutturaService.getAllStrutture();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    /**
     * Metodo che permette di modificare i dati di una struttura.
     * @param id Id della struttura da modificare.
     * @param request DTO con i nuovi dati da sovrascrivere.
     * @return DTO con i dati dell'utente modificati.
     */
    @PutMapping("/updateStruttura/{id}")
    public ResponseEntity<StrutturaResponse> updateStruttura(@PathVariable String id, @RequestBody CreaModificaStrutturaRequest request){

        long strutturaId = Long.parseLong(id);

        final StrutturaResponse response = strutturaService.updateStruttura(strutturaId,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    /**
     * Metodo per eliminare una struttura dal database tramite l'id.
     * @param id Id della struttura da eliminare.
     * @return Messaggio di risposta al client.
     */
    @DeleteMapping("/deleteStrutturaById/{id}")
    public ResponseEntity<MessageResponse> deleteStrutturaById(@PathVariable String id){

        long strutturaId = Long.parseLong(id);

        strutturaService.deleteStrutturaById(strutturaId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Struttura eliminata."));
    }

    /**
     *
     * @param request
     * @return
     */
    @PutMapping("/associaInfermiere")
    public ResponseEntity<MessageResponse> associaInfermiere(@RequestBody AssociaDissociaInfermiereRequest request){

        strutturaService.associaInfermiere(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Infermiere associato."));
    }

    @PutMapping("/dissociaInfermiere")
    public ResponseEntity<MessageResponse> dissociaInfermiere(@RequestBody AssociaDissociaInfermiereRequest request){

        strutturaService.dissociaInfermiere(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Infermiere dissociato."));
    }

}
