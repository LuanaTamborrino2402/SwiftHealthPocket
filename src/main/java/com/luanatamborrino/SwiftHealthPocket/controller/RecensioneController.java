package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.RecensioneRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.RecensioneResponse;
import com.luanatamborrino.SwiftHealthPocket.service.RecensioneService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per le recensioni.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recensione")
public class RecensioneController {

    private final RecensioneService recensioneService;

    /**
     * Metodo che salva una nuova recensione per una specifica prestazione.
     * @param idPrestazione Id della prestazione a cui la recensione è associata.
     * @param request DTO con i dati di ogni recensione.
     * @return Messaggio di risposta al client.
     */
    @PostMapping("/salva/{idPrestazione}")
    public ResponseEntity<MessageResponse> salva(
            @PathVariable String idPrestazione,
            @RequestBody RecensioneRequest request) {

        long id = Long.parseLong(idPrestazione);

        recensioneService.salva(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Recensione salvata."));
    }


    /**
     * Metodo che recupera tutte le recensioni associate a un paziente specifico.
     * @param idPaziente Id del paziente per il quale recuperare le recensioni.
     * @return Lista di DTO con i dati delle recensioni relative al paziente specificato.
     */
    @GetMapping("/getAllByPaziente/{idPaziente}")
    public ResponseEntity<List<RecensioneResponse>> getAllByPaziente(@PathVariable String idPaziente) {

        long id = Long.parseLong(idPaziente);

        List<RecensioneResponse> response =  recensioneService.getAllByPaziente(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo che recupera la recensione associata ad una specifica prestazione.
     * @param idPrestazione Id della prestazione per la quale reucperare la recensione
     * @return DTO con i dati della recensione.
     */
    @GetMapping("/getByPrestazioneId/{idPrestazione}")
    public ResponseEntity<RecensioneResponse> getByPrestazioneId(@PathVariable String idPrestazione) {

        long id = Long.parseLong(idPrestazione);

        RecensioneResponse response =  recensioneService.getByPrestazioneId(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}

