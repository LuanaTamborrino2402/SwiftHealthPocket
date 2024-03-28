package com.luanatamborrino.SwiftHealthPocket.controller;

import com.luanatamborrino.SwiftHealthPocket.dto.request.CreaStrutturaRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.MessageResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.StrutturaResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.UserResponse;
import com.luanatamborrino.SwiftHealthPocket.service.StrutturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/struttura")
public class StrutturaController {

    private final StrutturaService strutturaService;

    @PostMapping("/creaStruttura")
    public ResponseEntity<MessageResponse> creaStruttura (@RequestBody CreaStrutturaRequest request){

        strutturaService.creaStruttura(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Struttura registrata."));


    }

    @GetMapping("/getStrutturaData/{id}")
    public ResponseEntity<StrutturaResponse> getStrutturaData(@PathVariable String id){

        long strutturaId = Long.parseLong(id);

        final StrutturaResponse response = strutturaService.getStrutturaData(strutturaId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/getAllStrutture")
    public ResponseEntity<List<StrutturaResponse>> getAllStrutture(){

        final List<StrutturaResponse> response = strutturaService.getAllStrutture();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }
}
