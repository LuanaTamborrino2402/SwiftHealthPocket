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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recensione")
public class RecensioneController {
    private final RecensioneService recensioneService;

    @PostMapping("/salva/{idPrestazione}")
    public ResponseEntity<MessageResponse> salva(@PathVariable String idPrestazione, @RequestBody RecensioneRequest request){

        long id = Long.parseLong(idPrestazione);

        recensioneService.salva(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Recensione salvata."));
    }

    @GetMapping("/getAllByPaziente/{idPaziente}")
    public ResponseEntity<List<RecensioneResponse>> getAllByPaziente(@PathVariable String idPaziente){

        long id = Long.parseLong(idPaziente);

        List<RecensioneResponse> response =  recensioneService.getAllByPaziente(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/getByPrestazioneId/{idPrestazione}")
    public ResponseEntity<RecensioneResponse> getByPrestazioneId(@PathVariable String idPrestazione){

        long id = Long.parseLong(idPrestazione);

        RecensioneResponse response =  recensioneService.getByPrestazioneId(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}

