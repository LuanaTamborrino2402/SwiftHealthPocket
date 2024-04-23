package com.luanatamborrino.SwiftHealthPocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PrenotaPrestazioneRequest {

    private String tipoPrestazione;

    private LocalDateTime dataInizio;

    private Long idPaziente;

    private Long idStruttura;
}
