package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.PrenotaPrestazioneRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.PresaInCaricoRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.PrestazioneResponse;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prestazione")
public class PrestazioneController {

    private final PrestazioneService prestazioneService;

    @PostMapping("/prenota")
    public ResponseEntity<MessageResponse> prenota(@RequestBody PrenotaPrestazioneRequest request){

        prestazioneService.prenotaPrestazione(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Prestazione prenotata."));

    }

    @GetMapping("/getAllByPaziente/{idPaziente}")
    public ResponseEntity<List<PrestazioneResponse>> getAllByPaziente(@PathVariable String idPaziente){

        long id = Long.parseLong(idPaziente);

       List<PrestazioneResponse> response = prestazioneService.getAllByPaziente(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/eliminaPrenotazione/{idPrestazione}")
    public ResponseEntity<MessageResponse> eliminaPrenotazione(@PathVariable String idPrestazione){

        long id = Long.parseLong(idPrestazione);

        prestazioneService.eliminaPrenotazione(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Prenotazione eliminata."));
    }

    @GetMapping("/getAllPrenotazioni/{idStruttura}")
    public ResponseEntity<List<PrestazioneResponse>> getAllPrenotazioni(@PathVariable String idStruttura){

        long id = Long.parseLong(idStruttura);

        List<PrestazioneResponse> response = prestazioneService.getAllPrenotazioni(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/presaInCarico")
    public ResponseEntity<MessageResponse> presaInCarico(@RequestBody PresaInCaricoRequest request){

        prestazioneService.presaInCarico(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Prestazione presa in carico."));
    }
}
