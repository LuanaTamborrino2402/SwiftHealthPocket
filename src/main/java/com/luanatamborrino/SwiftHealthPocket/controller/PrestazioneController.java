package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.PrenotaPrestazioneRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prestazione")
public class PrestazioneController {

    private final PrestazioneService prestazioneService;

    @PostMapping("/prenotaPrestazione")
    public ResponseEntity<MessageResponse> prenotaPrestazione(@RequestBody PrenotaPrestazioneRequest request){

        prestazioneService.prenotaPrestazione(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Prestazione prenotata."));

    }
}
