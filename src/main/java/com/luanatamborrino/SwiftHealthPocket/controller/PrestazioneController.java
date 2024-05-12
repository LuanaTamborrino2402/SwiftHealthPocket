package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.EsitoRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.PrenotaPrestazioneRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.PresaInCaricoRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.PrestazioneResponse;
import com.luanatamborrino.SwiftHealthPocket.model._enum.EsitoPrestazione;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller per le prestazioni.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prestazione")
public class PrestazioneController {

    private final PrestazioneService prestazioneService;

    /**
     * Metodo che registra una nuova prenotazione per una prestazione.
     * @param request DTO con i dati della prestazione da prenotare.
     * @return Messaggio di risposta al client.
     */
    @PostMapping("/prenota")
    public ResponseEntity<MessageResponse> prenota(@RequestBody PrenotaPrestazioneRequest request){

        prestazioneService.prenotaPrestazione(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Prestazione prenotata."));

    }

    /**
     * Metodo che ottiente tutte le recensioni associate a un paziente specifico.
     * @param idPaziente Id del paziente per cui recuperare le prestazioni.
     * @return Lista di DTO con i dati delle prestazioni.
     */
    @GetMapping("/getAllByPaziente/{idPaziente}")
    public ResponseEntity<List<PrestazioneResponse>> getAllByPaziente(@PathVariable String idPaziente){

        long id = Long.parseLong(idPaziente);

       List<PrestazioneResponse> response = prestazioneService.getAllByPaziente(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo per eliminare una prenotazione esistente per una specifica prestazione
     * @param idPrestazione Id della prestazione la cui prenotazione deve essere cancellata.
     * @return Messaggio di risposta al client.
     */
    @DeleteMapping("/eliminaPrenotazione/{idPrestazione}")
    public ResponseEntity<MessageResponse> eliminaPrenotazione(@PathVariable String idPrestazione){

        long id = Long.parseLong(idPrestazione);

        prestazioneService.eliminaPrenotazione(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Prenotazione eliminata."));
    }

    /**
     * Metodo per recuperare tutte le prenotazioni di prestazioni per una specifica struttura.
     * @param idStruttura Id della struttura per cui recuperare le prenotazioni.
     * @return Lista di DTO con i dati delle prenotazioni.
     */
    @GetMapping("/getAllPrenotazioni/{idStruttura}")
    public ResponseEntity<List<PrestazioneResponse>> getAllPrenotazioni(@PathVariable String idStruttura){

        long id = Long.parseLong(idStruttura);

        List<PrestazioneResponse> response = prestazioneService.getAllPrenotazioni(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo che registra la presa in carico di una prestazione da parte di un infermiere.
     * @param request DTO con i dati della presa in carico della prestazione.
     * @return Messaggio di risposta al client.
     */
    @PostMapping("/presaInCarico")
    public ResponseEntity<MessageResponse> presaInCarico(@RequestBody PresaInCaricoRequest request){

        prestazioneService.presaInCarico(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Prestazione presa in carico."));
    }

    /**
     * Metodo che registra l'esito di una prestazione.
     * @param request DTO con i dati dell'esito da registrare.
     * @param idPrestazione Id della prestazione per la quale l'esito deve essere registrato.
     * @return Messaggio di risposta al client.
     */
    @PostMapping("/esito/{idPrestazione}")
    public ResponseEntity<MessageResponse> esito(@RequestBody EsitoRequest request,@PathVariable String idPrestazione){

        long id = Long.parseLong(idPrestazione);

        prestazioneService.esito(id,request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Esito inserito."));

    }

    /**
     * Metodo per ottenere tutte le prenotazioni di prestazioni per un dato paziente.
     * @param idPaziente Id del paziente per cui recuperare tutte le prenotazioni.
     * @return Lista di DTO con i dati delle prenotazioni.
     */
    @GetMapping("/getPrenotazioniByPaziente/{idPaziente}")
    public ResponseEntity<List<PrestazioneResponse>> getPrenotazioniByPaziente(@PathVariable String idPaziente){

        long id = Long.parseLong(idPaziente);

        List<PrestazioneResponse> response = prestazioneService.getPrenotazioniByPaziente(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo per cui recuperare tutte le prenotazioni gestite da un dato infermiere.
     * @param idInfermiere Id dell'infermiere per cui recuperare le prenotazioni.
     * @return Lista di DTO con i dati delle prenotazioni.
     */
    @GetMapping("/getPrenotazioniByInfermiere/{idInfermiere}")
    public ResponseEntity<List<PrestazioneResponse>> getPrenotazioniByInfermiere(@PathVariable String idInfermiere){

        long id = Long.parseLong(idInfermiere);

        List<PrestazioneResponse> response = prestazioneService.getPrenotazioniByInfermiere(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Metodo per ottenere lo storico delle prestazioni per uno specifico paziente.
     * @param idPaziente Id del paziente per cui recuperare lo storico delle prestazioni.
     * @return Lista di DTO con lo storico delle prestazioni.
     */
    @GetMapping("/storicoPrestazioni/{idPaziente}")
    public ResponseEntity<List<PrestazioneResponse>> storicoPrestazioni(@PathVariable String idPaziente){

        long id = Long.parseLong(idPaziente);

        List<PrestazioneResponse> response = prestazioneService.storicoPrestazioniByPaziente(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     *  Metodo che recupera lo storico di tutte le prestazioni nel sistema.
     * @return Lista di DTO con lo storico delle prestazioni.
     */
    @GetMapping("/storicoPrestazioni")
    public ResponseEntity<List<PrestazioneResponse>> storicoPrestazioni(){

        List<PrestazioneResponse> response = prestazioneService.storicoPrestazioni();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
